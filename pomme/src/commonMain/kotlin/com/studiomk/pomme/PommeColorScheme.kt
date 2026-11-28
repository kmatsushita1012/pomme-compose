package com.studiomk.pomme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * The semantic colors used by Pomme Compose for accents, text, and backgrounds.
 *
 * Each color can be replaced independently with [copy]. The [light] and [dark] factories provide
 * the standard palettes, independently of theme integration.
 */
@Immutable
public data class PommeColorScheme(
    /** The accent color for links and prominent controls. */
    val accentColor: Color,
    /** The color for primary text and other primary content. */
    val primary: Color,
    /** The color for secondary text and other secondary content. */
    val secondary: Color,
    /** The background color for non-grouped content. */
    val background: Color,
    /** The background color behind grouped content such as a form. */
    val groupedBackground: Color,
    /** The background color for content inside a grouped section. */
    val secondaryGroupedBackground: Color,
) {
    public companion object {
        /** Returns the standard light color palette. */
        public fun light(): PommeColorScheme =
            PommeColorScheme(
                accentColor = Color(0xFF007AFF),
                primary = Color.Black.copy(alpha = 0.85f),
                secondary = Color.Black.copy(alpha = 0.55f),
                background = Color(0xFFFFFFFF),
                groupedBackground = Color(0xFFF2F2F7),
                secondaryGroupedBackground = Color(0xFFFFFFFF),
            )

        /** Returns the standard dark color palette. */
        public fun dark(): PommeColorScheme =
            PommeColorScheme(
                accentColor = Color(0xFF0A84FF),
                primary = Color.White.copy(alpha = 0.85f),
                secondary = Color.White.copy(alpha = 0.55f),
                background = Color(0xFF000000),
                groupedBackground = Color(0xFF000000),
                secondaryGroupedBackground = Color(0xFF1C1C1E),
            )
    }
}

@Preview(
    name = "PommeColorScheme Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 480,
)
@Composable
internal fun PommeColorSchemeLightPreview() {
    PommeColorSchemePreview(
        colorScheme = PommeColorScheme.light(),
        themeName = "Light",
    )
}

@Preview(
    name = "PommeColorScheme Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 480,
)
@Composable
internal fun PommeColorSchemeDarkPreview() {
    PommeColorSchemePreview(
        colorScheme = PommeColorScheme.dark(),
        themeName = "Dark",
    )
}

@Composable
private fun PommeColorSchemePreview(
    colorScheme: PommeColorScheme,
    themeName: String,
) {
    val contentColor = colorScheme.background.contentColor()
    val outlineColor = if (colorScheme.background.luminance() > 0.5f) {
        Color(0xFF767680)
    } else {
        Color(0xFF8E8E93)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        BasicText(
            text = "PommeColorScheme · $themeName",
            style = TextStyle(
                color = contentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
        PommeColorSchemeSwatch(
            "Accent",
            colorScheme.accentColor.toPreviewValue(),
            colorScheme.accentColor,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeSwatch(
            "Primary text",
            colorScheme.primary.toPreviewValue(),
            colorScheme.primary,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeSwatch(
            "Secondary text",
            colorScheme.secondary.toPreviewValue(),
            colorScheme.secondary,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeSwatch(
            "Background",
            colorScheme.background.toPreviewValue(),
            colorScheme.background,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeSwatch(
            "Grouped background",
            colorScheme.groupedBackground.toPreviewValue(),
            colorScheme.groupedBackground,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeSwatch(
            "Secondary grouped background",
            colorScheme.secondaryGroupedBackground.toPreviewValue(),
            colorScheme.secondaryGroupedBackground,
            contentColor,
            outlineColor,
        )
    }
}

@Composable
private fun PommeColorSchemeSwatch(
    label: String,
    hex: String,
    color: Color,
    contentColor: Color,
    outlineColor: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            BasicText(
                text = label,
                style = TextStyle(color = contentColor, fontSize = 14.sp),
            )
            BasicText(
                text = hex,
                style = TextStyle(color = contentColor, fontSize = 12.sp),
            )
        }
        Box(
            modifier = Modifier
                .size(width = 96.dp, height = 48.dp)
                .background(color, RoundedCornerShape(6.dp))
                .border(1.dp, outlineColor, RoundedCornerShape(6.dp)),
        )
    }
}

private fun Color.contentColor(): Color = luminance().contentColor()

private fun Float.contentColor(): Color = if (this >= 0.175f) Color.Black else Color.White

private fun Color.toHex(): String {
    fun Float.toHexByte(): String {
        val value = (this * 255).roundToInt().coerceIn(0, 255)
        val digits = "0123456789ABCDEF"
        return "${digits[value shr 4]}${digits[value and 0x0F]}"
    }

    return "${red.toHexByte()}${green.toHexByte()}${blue.toHexByte()}"
}

private fun Color.toPreviewValue(): String {
    val opacity = if (alpha < 1f) " · ${(alpha * 100).roundToInt()}%" else ""
    return "#${toHex()}$opacity"
}
