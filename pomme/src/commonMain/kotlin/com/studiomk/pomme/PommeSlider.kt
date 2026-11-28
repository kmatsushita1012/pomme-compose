package com.studiomk.pomme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.floor

/**
 * A continuous or stepped control for selecting a value within a closed range.
 *
 * The caller owns [value] and updates it from [onValueChange]. Values supplied outside
 * [valueRange] are clamped for display only. A non-null [step] snaps user changes to values
 * measured from the lower end of [valueRange], while both ends of the range remain reachable.
 *
 * @param value The current value owned by the caller.
 * @param onValueChange Called when the user changes the value.
 * @param modifier Modifier applied to the slider row.
 * @param valueRange The finite range of selectable values. The lower end must be less than the
 *   upper end.
 * @param step Optional positive finite interval between values. When omitted, the slider is
 *   continuous.
 * @param enabled Whether the slider accepts user input.
 * @param tint The active track color. Defaults to the current theme's accent color.
 * @param onEditingChanged Called once when a user editing session starts and once when it ends or
 *   is cancelled.
 * @param minimumValueLabel Optional content displayed at the lower end of the slider.
 * @param maximumValueLabel Optional content displayed at the upper end of the slider.
 */
@Composable
public fun PommeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    step: Float? = null,
    enabled: Boolean = true,
    tint: Color? = null,
    onEditingChanged: (Boolean) -> Unit = {},
    minimumValueLabel: (@Composable () -> Unit)? = null,
    maximumValueLabel: (@Composable () -> Unit)? = null,
) {
    validatePommeSliderArguments(valueRange, step)

    val displayedValue = clampPommeSliderValue(value, valueRange)
    val progress = pommeSliderProgress(displayedValue, valueRange)
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val densityScale = androidx.compose.ui.platform.LocalDensity.current.density
    val colorScheme = PommeTheme.colorScheme
    val activeTint = tint ?: colorScheme.accentColor
    val activeTrackColor = if (enabled) activeTint else activeTint.copy(alpha = 0.38f)
    val inactiveTrackColor = colorScheme.secondary.copy(alpha = if (enabled) 0.18f else 0.10f)
    val thumbColor = Color.White.copy(alpha = if (enabled) 1f else 0.76f)
    val thumbOutlineColor = colorScheme.secondary.copy(alpha = 0.45f)

    var editing by remember { mutableStateOf(false) }
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    val latestValue = rememberUpdatedState(value)
    val latestRange = rememberUpdatedState(valueRange)
    val latestStep = rememberUpdatedState(step)
    val latestEnabled = rememberUpdatedState(enabled)
    val latestOnValueChange = rememberUpdatedState(onValueChange)
    val latestOnEditingChanged = rememberUpdatedState(onEditingChanged)

    fun beginEditing() {
        if (!editing) {
            editing = true
            latestOnEditingChanged.value(true)
        }
    }

    fun endEditing() {
        if (editing) {
            editing = false
            latestOnEditingChanged.value(false)
        }
    }

    fun updateFromUserValue(proposedValue: Float) {
        if (!latestEnabled.value) return
        val range = latestRange.value
        val nextValue = snapPommeSliderValue(proposedValue, range, latestStep.value)
        if (nextValue != latestValue.value) {
            latestOnValueChange.value(nextValue)
        }
    }

    fun valueAtPosition(positionX: Float, thumbOffsetPx: Float = 0f): Float {
        val range = latestRange.value
        val thumbRadiusPx = POMME_SLIDER_THUMB_SIZE.value * densityScale / 2f
        val usableWidth = (trackWidthPx - thumbRadiusPx * 2f).coerceAtLeast(0f)
        if (usableWidth == 0f) return range.start
        val physicalProgress = ((positionX + thumbOffsetPx - thumbRadiusPx) / usableWidth)
            .coerceIn(0f, 1f)
        val rangeProgress = if (isRtl) 1f - physicalProgress else physicalProgress
        return range.start + ((range.endInclusive - range.start) * rangeProgress)
    }

    LaunchedEffect(enabled) {
        if (!enabled) endEditing()
    }
    DisposableEffect(Unit) {
        onDispose { endEditing() }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (minimumValueLabel != null) {
            Box(contentAlignment = Alignment.CenterStart) { minimumValueLabel() }
        }

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .height(POMME_SLIDER_TOUCH_TARGET_HEIGHT)
                .onSizeChanged { trackWidthPx = it.width.toFloat() }
                .pointerInput(enabled) {
                    if (enabled) {
                        detectTapGestures { position ->
                            beginEditing()
                            try {
                                updateFromUserValue(valueAtPosition(position.x))
                            } finally {
                                endEditing()
                            }
                        }
                    }
                }
                .pointerInput(enabled, isRtl) {
                    if (enabled) {
                        var thumbOffsetPx = 0f
                        detectDragGestures(
                            onDragStart = { position ->
                                beginEditing()
                                val currentRange = latestRange.value
                                val currentProgress = pommeSliderProgress(
                                    clampPommeSliderValue(latestValue.value, currentRange),
                                    currentRange,
                                )
                                val radiusPx = POMME_SLIDER_THUMB_SIZE.value * densityScale / 2f
                                val width = trackWidthPx
                                val currentThumbX = if (isRtl) {
                                    width - radiusPx - (width - radiusPx * 2f) * currentProgress
                                } else {
                                    radiusPx + (width - radiusPx * 2f) * currentProgress
                                }
                                thumbOffsetPx = if (abs(position.x - currentThumbX) <= radiusPx * 1.5f) {
                                    currentThumbX - position.x
                                } else {
                                    0f
                                }
                                if (thumbOffsetPx == 0f) {
                                    updateFromUserValue(valueAtPosition(position.x))
                                }
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                updateFromUserValue(valueAtPosition(change.position.x, thumbOffsetPx))
                            },
                            onDragEnd = ::endEditing,
                            onDragCancel = ::endEditing,
                        )
                    }
                }
                .semantics {
                    if (!enabled) disabled()
                    progressBarRangeInfo = ProgressBarRangeInfo(
                        current = displayedValue,
                        range = valueRange,
                        steps = pommeSliderSemanticSteps(valueRange, step),
                    )
                    if (enabled) {
                        setProgress { targetValue ->
                            beginEditing()
                            try {
                                updateFromUserValue(targetValue)
                            } finally {
                                endEditing()
                            }
                            true
                        }
                    }
                }
                .focusable(enabled),
        ) {
            val localDensity = androidx.compose.ui.platform.LocalDensity.current
            val thumbSizePx = with(localDensity) { POMME_SLIDER_THUMB_SIZE.toPx() }
            val usableWidthPx = (constraints.maxWidth.toFloat() - thumbSizePx).coerceAtLeast(0f)
            val thumbStartOffset = with(localDensity) {
                (usableWidthPx * progress).toDp()
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val thumbRadiusPx = POMME_SLIDER_THUMB_SIZE.value * density / 2f
                val trackStart = thumbRadiusPx
                val trackEnd = (size.width - thumbRadiusPx).coerceAtLeast(trackStart)
                val centerY = size.height / 2f
                val thumbX = if (isRtl) {
                    trackEnd - (trackEnd - trackStart) * progress
                } else {
                    trackStart + (trackEnd - trackStart) * progress
                }
                val strokeWidth = POMME_SLIDER_TRACK_HEIGHT.value * density

                drawLine(
                    color = inactiveTrackColor,
                    start = Offset(trackStart, centerY),
                    end = Offset(trackEnd, centerY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
                drawLine(
                    color = activeTrackColor,
                    start = if (isRtl) Offset(thumbX, centerY) else Offset(trackStart, centerY),
                    end = if (isRtl) Offset(trackEnd, centerY) else Offset(thumbX, centerY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = thumbStartOffset)
                    .size(POMME_SLIDER_THUMB_SIZE)
                    .shadow(elevation = 2.dp, shape = CircleShape)
                    .background(color = thumbColor, shape = CircleShape)
                    .border(width = 1.dp, color = thumbOutlineColor, shape = CircleShape),
            )
        }

        if (maximumValueLabel != null) {
            Box(contentAlignment = Alignment.CenterEnd) { maximumValueLabel() }
        }
    }
}

private val POMME_SLIDER_THUMB_SIZE = 28.dp
private val POMME_SLIDER_TRACK_HEIGHT = 4.dp
private val POMME_SLIDER_TOUCH_TARGET_HEIGHT = 48.dp

/** Validates the range and optional step before any slider UI is rendered. */
internal fun validatePommeSliderArguments(
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float?,
) {
    require(
        valueRange.start.isFinite() && valueRange.endInclusive.isFinite() &&
            valueRange.start < valueRange.endInclusive,
    ) {
        "valueRange must contain finite bounds with lower bound less than upper bound"
    }
    require(step == null || (step.isFinite() && step > 0f)) {
        "step must be a finite positive value when specified"
    }
}

/** Clamps only the displayed value, leaving the caller-owned value untouched. */
internal fun clampPommeSliderValue(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float = if (value.isNaN()) valueRange.start else value.coerceIn(valueRange.start, valueRange.endInclusive)

/** Snaps a user-proposed value to the nearest valid stop, including both range endpoints. */
internal fun snapPommeSliderValue(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float?,
): Float {
    val clampedValue = clampPommeSliderValue(value, valueRange)
    if (step == null || clampedValue == valueRange.start || clampedValue == valueRange.endInclusive) {
        return clampedValue
    }

    val lowerBound = valueRange.start.toDouble()
    val upperBound = valueRange.endInclusive.toDouble()
    val stepSize = step.toDouble()
    val position = (clampedValue.toDouble() - lowerBound) / stepSize
    val lowerIndex = floor(position)
    val lowerStop = (lowerBound + lowerIndex * stepSize).coerceIn(lowerBound, upperBound)
    val upperStop = (lowerBound + (lowerIndex + 1.0) * stepSize).coerceAtMost(upperBound)
    val snappedValue = if (clampedValue.toDouble() - lowerStop < upperStop - clampedValue.toDouble()) {
        lowerStop
    } else {
        upperStop
    }
    return snappedValue.toFloat().coerceIn(valueRange.start, valueRange.endInclusive)
}

/** Returns the number of intermediate selectable stops reported to accessibility services. */
internal fun pommeSliderSemanticSteps(
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float?,
): Int {
    if (step == null) return 0
    val intervalCount = (valueRange.endInclusive.toDouble() - valueRange.start.toDouble()) / step.toDouble()
    if (!intervalCount.isFinite() || intervalCount >= Int.MAX_VALUE.toDouble()) return Int.MAX_VALUE

    val nearestWholeCount = kotlin.math.round(intervalCount)
    val tolerance = 1e-6 * maxOf(1.0, intervalCount)
    val includesUpperBound = abs(intervalCount - nearestWholeCount) <= tolerance
    val intermediateStops = if (includesUpperBound) {
        nearestWholeCount.toInt() - 1
    } else {
        floor(intervalCount).toInt()
    }
    return intermediateStops.coerceAtLeast(0)
}

private fun pommeSliderProgress(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
): Float = (
    (value.toDouble() - valueRange.start.toDouble()) /
        (valueRange.endInclusive.toDouble() - valueRange.start.toDouble())
    ).toFloat().coerceIn(0f, 1f)

@PommeThemeLightDarkPreview
@Composable
internal fun PommeSliderDefaultPreview() {
    PommeSliderPreviewContent {
        PommeSlider(value = 0.64f, onValueChange = {})
    }
}

@PommeThemeLightDarkPreview
@Composable
internal fun PommeSliderLabelsPreview() {
    PommeSliderPreviewContent {
        PommeSlider(
            value = 0.42f,
            onValueChange = {},
            minimumValueLabel = {
                BasicText("Min", style = PommeTheme.typography.caption.copy(fontSize = 12.sp))
            },
            maximumValueLabel = {
                BasicText("Max", style = PommeTheme.typography.caption.copy(fontSize = 12.sp))
            },
        )
    }
}

@PommeThemeLightDarkPreview
@Composable
internal fun PommeSliderTintPreview() {
    PommeSliderPreviewContent {
        PommeSlider(value = 0.7f, onValueChange = {}, tint = Color(0xFFAF52DE))
    }
}

@PommeThemeLightDarkPreview
@Composable
internal fun PommeSliderDisabledPreview() {
    PommeSliderPreviewContent {
        PommeSlider(value = 0.48f, onValueChange = {}, enabled = false)
    }
}

@PommeThemeLightDarkPreview
@Composable
internal fun PommeSliderStepPreview() {
    PommeSliderPreviewContent {
        PommeSlider(value = 0.9f, onValueChange = {}, valueRange = 0f..1f, step = 0.3f)
    }
}

@Composable
private fun PommeSliderPreviewContent(content: @Composable () -> Unit) {
    val scheme = if (androidx.compose.foundation.isSystemInDarkTheme()) {
        PommeColorScheme.dark()
    } else {
        PommeColorScheme.light()
    }
    PommeTheme(colorScheme = scheme) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(scheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            content()
        }
    }
}
