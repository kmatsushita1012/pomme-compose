package com.studiomk.pomme.sample

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

@PreviewTest
@Preview(
    name = "Sample Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PommeSampleLightScreenshotTest() {
    PommeSampleLightPreview()
}

@PreviewTest
@Preview(
    name = "Sample Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PommeSampleDarkScreenshotTest() {
    PommeSampleDarkPreview()
}
