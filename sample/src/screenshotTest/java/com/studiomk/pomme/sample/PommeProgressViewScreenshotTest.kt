package com.studiomk.pomme.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.studiomk.pomme.PommeColorScheme
import com.studiomk.pomme.PommeProgressView
import com.studiomk.pomme.PommeProgressViewStyle
import com.studiomk.pomme.PommeTheme

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
internal annotation class PommeProgressViewScreenshotPreviews

@PreviewTest
@PommeProgressViewScreenshotPreviews
@Composable
fun PommeProgressViewAutomaticScreenshotTest() {
    PommeProgressViewScreenshotFixture(PommeProgressViewStyle.Automatic)
}

@PreviewTest
@PommeProgressViewScreenshotPreviews
@Composable
fun PommeProgressViewCircularScreenshotTest() {
    PommeProgressViewScreenshotFixture(PommeProgressViewStyle.Circular)
}

@PreviewTest
@PommeProgressViewScreenshotPreviews
@Composable
fun PommeProgressViewLinearScreenshotTest() {
    PommeProgressViewScreenshotFixture(PommeProgressViewStyle.Linear)
}

@Composable
private fun PommeProgressViewScreenshotFixture(style: PommeProgressViewStyle) {
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
                tint = androidx.compose.ui.graphics.Color(0xFFAF52DE),
            )
        }
    }
}
