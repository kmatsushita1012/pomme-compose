package com.studiomk.pomme.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.studiomk.pomme.PommeColorScheme
import com.studiomk.pomme.PommeTheme
import com.studiomk.pomme.PommeTypography
import kotlin.math.roundToInt

@Composable
internal fun PommeColorSchemeScreenshotFixture() {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) PommeColorScheme.dark() else PommeColorScheme.light()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        BasicText(
            text = "PommeColorScheme · ${if (darkTheme) "Dark" else "Light"}",
            style = TextStyle(
                color = colorScheme.primary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )

        listOf(
            "Accent" to colorScheme.accentColor,
            "Primary text" to colorScheme.primary,
            "Secondary text" to colorScheme.secondary,
            "Background" to colorScheme.background,
            "Grouped background" to colorScheme.groupedBackground,
            "Secondary grouped background" to colorScheme.secondaryGroupedBackground,
        ).forEach { (label, color) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    BasicText(
                        text = label,
                        style = TextStyle(color = colorScheme.primary, fontSize = 14.sp),
                    )
                    BasicText(
                        text = color.toPreviewValue(),
                        style = TextStyle(color = colorScheme.secondary, fontSize = 12.sp),
                    )
                }
                Box(
                    modifier = Modifier
                        .size(width = 96.dp, height = 48.dp)
                        .background(color, RoundedCornerShape(6.dp))
                        .border(
                            width = 1.dp,
                            color = colorScheme.secondary,
                            shape = RoundedCornerShape(6.dp),
                        ),
                )
            }
        }
    }
}

@Composable
internal fun PommeTypographyScreenshotFixture(style: TextStyle) {
    val darkTheme = isSystemInDarkTheme()
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

@Composable
internal fun PommeThemeScreenshotFixture() {
    val darkTheme = isSystemInDarkTheme()

    MaterialTheme(colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()) {
        PommeTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PommeTheme.colorScheme.background)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "PommeTheme · ${if (darkTheme) "Dark" else "Light"}",
                    style = PommeTheme.typography.headline,
                )
                Text(text = "Default body text")
                Text(text = "Explicit title style", style = PommeTheme.typography.title)
                Text(text = "Explicit bold weight", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
internal fun PommeThemeOverrideScreenshotFixture() {
    val darkTheme = isSystemInDarkTheme()

    MaterialTheme(colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()) {
        PommeTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PommeTheme.colorScheme.background)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "Parent theme before override")
                PommeTheme(
                    colorScheme = PommeColorScheme.light().copy(
                        accentColor = Color(0xFF006B8F),
                        primary = Color(0xFF003B57),
                        secondary = Color(0xFF3D5968),
                        background = Color(0xFFEAF6FF),
                        groupedBackground = Color(0xFFD7EEFF),
                    ),
                    typography = PommeTypography(
                        body = PommeTypography().body.copy(fontSize = 20.sp, lineHeight = 26.sp),
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PommeTheme.colorScheme.background)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(text = "Nested explicit theme", style = PommeTheme.typography.headline)
                        Text(text = "Overridden body text")
                    }
                }

                Text(text = "Parent theme after override")
            }
        }
    }
}

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
