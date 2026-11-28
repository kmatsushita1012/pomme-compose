package com.studiomk.pomme.sample

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
internal annotation class PommeSampleScreenshotPreviews

@PreviewTest
@PommeSampleScreenshotPreviews
@Composable
fun PommeSampleScreenshotTest() {
    PommeSampleContent(darkTheme = isSystemInDarkTheme())
}
