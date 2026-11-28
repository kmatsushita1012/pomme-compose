package com.studiomk.pomme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Internal inputs shared by the eager and lazy row containers. */
@Immutable
internal data class PommeRowMetrics(
    val horizontalInset: Dp,
    val verticalPadding: Dp,
    val minimumHeight: Dp,
    val dividerInset: Dp,
    val dividerThickness: Dp,
    val dividerColor: Color,
)

/**
 * Measures each direct layout child as one row and draws the shared row treatment around it.
 *
 * A child that emits no layout node creates no row. If content emits multiple direct layout
 * nodes, each node becomes a row; use a standard Row, Column, or Box to group nodes into one row.
 */
@Composable
internal fun PommeRowsLayout(
    modifier: Modifier = Modifier,
    metrics: PommeRowMetrics,
    surfaceColor: Color?,
    drawTrailingDivider: Boolean = false,
    content: @Composable () -> Unit,
) {
    val dividerPositions = remember { mutableListOf<Float>() }

    Layout(
        modifier = modifier.drawWithContent {
            surfaceColor?.let { drawRect(color = it) }
            drawContent()

            val strokeWidth = metrics.dividerThickness.toPx()
            if (strokeWidth > 0f) {
                val inset = metrics.dividerInset.toPx()
                val lineStart = inset.coerceAtMost(size.width)
                val lineEnd = (size.width - inset).coerceAtLeast(lineStart)
                dividerPositions.forEach { y ->
                    drawLine(
                        color = metrics.dividerColor,
                        start = Offset(lineStart, y - strokeWidth / 2f),
                        end = Offset(lineEnd, y - strokeWidth / 2f),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Butt,
                    )
                }
            }
        },
        content = content,
    ) { measurables, constraints ->
        measurePommeRows(
            measurables = measurables,
            constraints = constraints,
            metrics = metrics,
            drawTrailingDivider = drawTrailingDivider,
            dividerPositions = dividerPositions,
        )
    }
}

/** A keyed lazy-list fixture using the same internal row renderer as [PommeRowsLayout]. */
internal data class PommeLazyRowItem(
    val key: Any,
    val content: @Composable () -> Unit,
)

@Composable
internal fun PommeLazyRows(
    items: List<PommeLazyRowItem>,
    modifier: Modifier = Modifier,
    metrics: PommeRowMetrics,
    surfaceColor: Color,
) {
    LazyColumn(
        modifier = modifier.background(surfaceColor),
    ) {
        itemsIndexed(
            items = items,
            key = { _, item -> item.key },
            contentType = { _, _ -> PommeLazyRowItemContentType },
        ) { index, item ->
            PommeRowsLayout(
                modifier = Modifier.fillMaxWidth(),
                metrics = metrics,
                surfaceColor = null,
                drawTrailingDivider = index < items.lastIndex,
            ) {
                item.content()
            }
        }
    }
}

private fun MeasureScope.measurePommeRows(
    measurables: List<Measurable>,
    constraints: Constraints,
    metrics: PommeRowMetrics,
    drawTrailingDivider: Boolean,
    dividerPositions: MutableList<Float>,
): androidx.compose.ui.layout.MeasureResult {
    val insetPx = metrics.horizontalInset.roundToPx()
    val dividerInsetPx = metrics.dividerInset.roundToPx()
    val verticalPaddingPx = metrics.verticalPadding.roundToPx()
    val minimumHeightPx = metrics.minimumHeight.roundToPx()
    val maxContentWidth = if (constraints.hasBoundedWidth) {
        (constraints.maxWidth - insetPx * 2).coerceAtLeast(0)
    } else {
        Constraints.Infinity
    }
    val placeables = measurables.map { measurable ->
        measurable.measure(
            Constraints(
                maxWidth = maxContentWidth,
                maxHeight = constraints.maxHeight,
            ),
        )
    }
    val rowHeights = placeables.map { placeable ->
        maxOf(minimumHeightPx, placeable.height + verticalPaddingPx * 2)
    }
    val desiredWidth = if (constraints.hasBoundedWidth) {
        constraints.maxWidth
    } else {
        (placeables.maxOfOrNull { it.width } ?: 0) + insetPx * 2
    }
    val width = desiredWidth.coerceIn(constraints.minWidth, constraints.maxWidth)
    val height = rowHeights.sum().coerceIn(constraints.minHeight, constraints.maxHeight)

    dividerPositions.clear()
    var y = 0
    rowHeights.forEachIndexed { index, rowHeight ->
        y += rowHeight
        if (index < rowHeights.lastIndex || drawTrailingDivider) {
            dividerPositions += y.toFloat()
        }
    }

    return layout(width, height) {
        var rowTop = 0
        placeables.forEachIndexed { index, placeable ->
            val rowHeight = rowHeights[index]
            val childTop = rowTop + ((rowHeight - placeable.height) / 2)
            placeable.placeRelative(x = insetPx, y = childTop)
            rowTop += rowHeight
        }
    }
}

private const val PommeLazyRowItemContentType = "pomme-row"

private val PommeRowsPreviewMetrics = PommeRowMetrics(
    horizontalInset = 16.dp,
    verticalPadding = 8.dp,
    minimumHeight = 44.dp,
    dividerInset = 16.dp,
    dividerThickness = 0.5.dp,
    dividerColor = Color(0x4D8E8E93),
)

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 560,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 560,
)
@Composable
internal fun PommeRowsLayoutPreview() {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (darkTheme) {
            androidx.compose.material3.darkColorScheme()
        } else {
            androidx.compose.material3.lightColorScheme()
        },
    ) {
        PommeTheme {
            var showOptionalRow by remember { mutableStateOf(true) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PommeTheme.colorScheme.groupedBackground)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Direct children and grouped layouts", style = PommeTheme.typography.headline)
                TextButton(onClick = { showOptionalRow = !showOptionalRow }) {
                    Text(if (showOptionalRow) "Hide optional row" else "Show optional row")
                }
                PommeRowsLayout(
                    modifier = Modifier.fillMaxWidth(),
                    metrics = PommeRowsPreviewMetrics.copy(
                        dividerColor = PommeTheme.colorScheme.secondary.copy(alpha = 0.35f),
                    ),
                    surfaceColor = PommeTheme.colorScheme.secondaryGroupedBackground,
                ) {
                    Text("Direct Text child")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Grouped in a Row")
                        Text("Value")
                    }
                    Column {
                        Text("Grouped in a Column")
                        Text("A supporting line", color = PommeTheme.colorScheme.secondary)
                    }
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text("Grouped in a Box")
                    }
                    if (showOptionalRow) Text("Conditional direct child")
                    Text(
                        "Long text wraps within the row and increases its height when needed.",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 340,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 340,
)
@Composable
internal fun PommeLazyRowsPreview() {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (darkTheme) {
            androidx.compose.material3.darkColorScheme()
        } else {
            androidx.compose.material3.lightColorScheme()
        },
    ) {
        PommeTheme {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PommeTheme.colorScheme.groupedBackground)
                    .padding(16.dp),
            ) {
                Text("Lazy rows with stable keys", style = PommeTheme.typography.headline)
                Spacer(Modifier.height(8.dp))
                PommeLazyRows(
                    items = (0 until 100).map { index ->
                        PommeLazyRowItem(key = index) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text("Item $index")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    metrics = PommeRowsPreviewMetrics.copy(
                        dividerColor = PommeTheme.colorScheme.secondary.copy(alpha = 0.35f),
                    ),
                    surfaceColor = PommeTheme.colorScheme.secondaryGroupedBackground,
                )
            }
        }
    }
}
