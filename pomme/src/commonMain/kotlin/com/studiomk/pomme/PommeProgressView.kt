package com.studiomk.pomme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** The visual style used by [PommeProgressView]. */
@Immutable
public enum class PommeProgressViewStyle {
    /** Selects [Circular] for indeterminate progress and [Linear] for determinate progress. */
    Automatic,

    /** Shows a circular progress indicator. */
    Circular,

    /** Shows a horizontal progress indicator. */
    Linear,
}

/**
 * Shows progress toward completion of a task.
 *
 * When [value] is `null`, the progress is indeterminate. Otherwise, the displayed progress is
 * `value / total`, clamped to the range from zero to one. This composable observes changes to
 * [value] and [total] directly and does not retain progress state.
 *
 * [PommeProgressViewStyle.Automatic] uses a circular indicator for indeterminate progress and a
 * linear indicator for determinate progress. Circular indicators use the theme's secondary color
 * by default; linear indicators use its accent color. An explicit [tint] overrides either default.
 *
 * @param value The completed amount, or `null` to show indeterminate progress.
 * @param total The positive finite amount that represents completion.
 * @param modifier The modifier applied to this progress view.
 * @param style The visual style. Defaults to [PommeProgressViewStyle.Automatic].
 * @param tint The indicator color, or `null` to use the style's theme color.
 * @param label Optional content describing the task.
 * @param currentValueLabel Optional content describing the current progress value.
 *
 * @throws IllegalArgumentException if [total] is not finite and greater than zero, or if [value]
 *   is not finite.
 */
@Composable
public fun PommeProgressView(
    value: Float? = null,
    total: Float = 1f,
    modifier: Modifier = Modifier,
    style: PommeProgressViewStyle = PommeProgressViewStyle.Automatic,
    tint: Color? = null,
    label: (@Composable () -> Unit)? = null,
    currentValueLabel: (@Composable () -> Unit)? = null,
) {
    val progress = progressFraction(value, total)
    val resolvedStyle = resolveProgressViewStyle(style, value)
    val colorScheme = PommeTheme.colorScheme
    val indicatorColor = tint ?: when (resolvedStyle) {
        PommeProgressViewStyle.Circular -> colorScheme.secondary
        PommeProgressViewStyle.Linear -> colorScheme.accentColor
        PommeProgressViewStyle.Automatic -> error("Automatic progress style must be resolved")
    }
    val trackColor = indicatorColor.copy(alpha = indicatorColor.alpha * 0.24f)
    val semanticsModifier = modifier.semantics(mergeDescendants = true) {
        progressBarRangeInfo = progressBarRangeInfo(progress)
    }

    when (resolvedStyle) {
        PommeProgressViewStyle.Circular -> {
            if (label == null && currentValueLabel == null) {
                PommeCircularProgressIndicator(
                    progress = progress,
                    color = indicatorColor,
                    trackColor = trackColor,
                    modifier = semanticsModifier,
                )
            } else {
                Row(
                    modifier = semanticsModifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    PommeCircularProgressIndicator(
                        progress = progress,
                        color = indicatorColor,
                        trackColor = trackColor,
                        modifier = Modifier.size(24.dp),
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        label?.invoke()
                        currentValueLabel?.invoke()
                    }
                }
            }
        }

        PommeProgressViewStyle.Linear -> {
            if (label == null && currentValueLabel == null) {
                PommeLinearProgressIndicator(
                    progress = progress,
                    color = indicatorColor,
                    trackColor = trackColor,
                    modifier = semanticsModifier.fillMaxWidth(),
                )
            } else {
                Column(
                    modifier = semanticsModifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        if (label != null) label()
                        if (currentValueLabel != null) currentValueLabel()
                    }
                    PommeLinearProgressIndicator(
                        progress = progress,
                        color = indicatorColor,
                        trackColor = trackColor,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        PommeProgressViewStyle.Automatic -> error("Automatic progress style must be resolved")
    }
}

internal fun resolveProgressViewStyle(
    style: PommeProgressViewStyle,
    value: Float?,
): PommeProgressViewStyle = when (style) {
    PommeProgressViewStyle.Automatic ->
        if (value == null) PommeProgressViewStyle.Circular else PommeProgressViewStyle.Linear

    PommeProgressViewStyle.Circular,
    PommeProgressViewStyle.Linear,
    -> style
}

internal fun progressBarRangeInfo(progress: Float?): ProgressBarRangeInfo =
    progress?.let { ProgressBarRangeInfo(it, 0f..1f) } ?: ProgressBarRangeInfo.Indeterminate

internal fun progressFraction(value: Float?, total: Float): Float? {
    require(total.isFinite() && total > 0f) {
        "total must be finite and greater than zero."
    }
    require(value == null || value.isFinite()) {
        "value must be finite when provided."
    }
    return value?.div(total)?.coerceIn(0f, 1f)
}

@Composable
private fun PommeCircularProgressIndicator(
    progress: Float?,
    color: Color,
    trackColor: Color,
    modifier: Modifier,
) {
    if (progress == null) {
        CircularProgressIndicator(
            modifier = modifier,
            color = color,
            trackColor = trackColor,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    } else {
        CircularProgressIndicator(
            progress = { progress },
            modifier = modifier,
            color = color,
            trackColor = trackColor,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    }
}

@Composable
private fun PommeLinearProgressIndicator(
    progress: Float?,
    color: Color,
    trackColor: Color,
    modifier: Modifier,
) {
    if (progress == null) {
        LinearProgressIndicator(
            modifier = modifier,
            color = color,
            trackColor = trackColor,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    } else {
        LinearProgressIndicator(
            progress = { progress },
            modifier = modifier,
            color = color,
            trackColor = trackColor,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    }
}

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 400,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 400,
)
internal annotation class PommeProgressViewLightDarkPreview

@PommeProgressViewLightDarkPreview
@Composable
internal fun PommeProgressViewAutomaticPreview() {
    PommeProgressViewPreviewContent(style = PommeProgressViewStyle.Automatic)
}

@PommeProgressViewLightDarkPreview
@Composable
internal fun PommeProgressViewCircularPreview() {
    PommeProgressViewPreviewContent(style = PommeProgressViewStyle.Circular)
}

@PommeProgressViewLightDarkPreview
@Composable
internal fun PommeProgressViewLinearPreview() {
    PommeProgressViewPreviewContent(style = PommeProgressViewStyle.Linear)
}

@Composable
private fun PommeProgressViewPreviewContent(style: PommeProgressViewStyle) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) PommeColorScheme.dark() else PommeColorScheme.light()

    PommeTheme(colorScheme = colorScheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(text = "${style.name} · ${if (darkTheme) "Dark" else "Light"}")
            PommeProgressView(
                style = style,
                label = { Text("Preparing upload") },
                currentValueLabel = { Text("Waiting") },
            )
            PommeProgressView(
                value = 0.6f,
                style = style,
                label = { Text("Uploading") },
                currentValueLabel = { Text("60%") },
            )
            PommeProgressView(value = 0f, style = style)
            PommeProgressView(value = 1f, style = style)
            PommeProgressView(
                value = 0.4f,
                style = style,
                tint = Color(0xFFAF52DE),
            )
        }
    }
}
