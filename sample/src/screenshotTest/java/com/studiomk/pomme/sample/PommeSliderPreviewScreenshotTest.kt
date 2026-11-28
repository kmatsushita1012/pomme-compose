package com.studiomk.pomme.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.studiomk.pomme.PommeColorScheme
import com.studiomk.pomme.PommeSlider
import com.studiomk.pomme.PommeTheme

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 128,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 128,
)
internal annotation class PommeSliderScreenshotPreviews

@PreviewTest
@PommeSliderScreenshotPreviews
@Composable
fun PommeSliderDefaultScreenshotTest() {
    PommeSliderScreenshotFixture {
        PommeSlider(value = 0.64f, onValueChange = {})
    }
}

@PreviewTest
@PommeSliderScreenshotPreviews
@Composable
fun PommeSliderLabelsScreenshotTest() {
    PommeSliderScreenshotFixture {
        PommeSlider(
            value = 0.42f,
            onValueChange = {},
            minimumValueLabel = {
                BasicText(
                    text = "Min",
                    style = PommeTheme.typography.caption.copy(color = PommeTheme.colorScheme.secondary),
                )
            },
            maximumValueLabel = {
                BasicText(
                    text = "Max",
                    style = PommeTheme.typography.caption.copy(color = PommeTheme.colorScheme.secondary),
                )
            },
        )
    }
}

@PreviewTest
@PommeSliderScreenshotPreviews
@Composable
fun PommeSliderTintScreenshotTest() {
    PommeSliderScreenshotFixture {
        PommeSlider(value = 0.7f, onValueChange = {}, tint = Color(0xFFAF52DE))
    }
}

@PreviewTest
@PommeSliderScreenshotPreviews
@Composable
fun PommeSliderDisabledScreenshotTest() {
    PommeSliderScreenshotFixture {
        PommeSlider(value = 0.48f, onValueChange = {}, enabled = false)
    }
}

@PreviewTest
@PommeSliderScreenshotPreviews
@Composable
fun PommeSliderStepScreenshotTest() {
    PommeSliderScreenshotFixture {
        PommeSlider(value = 0.9f, onValueChange = {}, valueRange = 0f..1f, step = 0.3f)
    }
}

@Composable
private fun PommeSliderScreenshotFixture(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) PommeColorScheme.dark() else PommeColorScheme.light()
    PommeTheme(colorScheme = colorScheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            content()
        }
    }
}
