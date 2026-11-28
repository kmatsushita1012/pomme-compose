package com.studiomk.pomme.sample

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studiomk.pomme.PommeColorScheme
import kotlin.math.roundToInt

@Composable
internal fun PommeColorSchemeScreenshotPreview(darkTheme: Boolean) {
    val colorScheme = if (darkTheme) PommeColorScheme.dark() else PommeColorScheme.light()
    val themeName = if (darkTheme) "Dark" else "Light"
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
        PommeColorSchemeScreenshotSwatch(
            "Accent",
            colorScheme.accentColor.toPreviewValue(),
            colorScheme.accentColor,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeScreenshotSwatch(
            "Primary text",
            colorScheme.primary.toPreviewValue(),
            colorScheme.primary,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeScreenshotSwatch(
            "Secondary text",
            colorScheme.secondary.toPreviewValue(),
            colorScheme.secondary,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeScreenshotSwatch(
            "Background",
            colorScheme.background.toPreviewValue(),
            colorScheme.background,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeScreenshotSwatch(
            "Grouped background",
            colorScheme.groupedBackground.toPreviewValue(),
            colorScheme.groupedBackground,
            contentColor,
            outlineColor,
        )
        PommeColorSchemeScreenshotSwatch(
            "Secondary grouped background",
            colorScheme.secondaryGroupedBackground.toPreviewValue(),
            colorScheme.secondaryGroupedBackground,
            contentColor,
            outlineColor,
        )
    }
}

@Composable
private fun PommeColorSchemeScreenshotSwatch(
    label: String,
    value: String,
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
            BasicText(text = label, style = TextStyle(color = contentColor, fontSize = 14.sp))
            BasicText(text = value, style = TextStyle(color = contentColor, fontSize = 12.sp))
        }
        Box(
            modifier = Modifier
                .size(width = 96.dp, height = 48.dp)
                .background(color, RoundedCornerShape(6.dp))
                .border(1.dp, outlineColor, RoundedCornerShape(6.dp)),
        )
    }
}

@Composable
internal fun PommeTypographyScreenshotPreview(style: TextStyle, darkTheme: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (darkTheme) Color(0xFF121212) else Color.White)
            .padding(24.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicText(
            text = "Sample text",
            style = style.copy(
                color = if (darkTheme) Color(0xFFE6E1E5) else Color(0xFF1B1B1B),
            ),
        )
    }
}

private fun Color.contentColor(): Color =
    if (luminance() > 0.5f) Color.Black else Color.White

private fun Color.toPreviewValue(): String {
    val opacity = if (alpha < 1f) " · ${(alpha * 100).roundToInt()}%" else ""
    return "#${toHex()}$opacity"
}

private fun Color.toHex(): String {
    val redValue = (red * 255).roundToInt()
    val greenValue = (green * 255).roundToInt()
    val blueValue = (blue * 255).roundToInt()
    return "%02X%02X%02X".format(redValue, greenValue, blueValue)
}
