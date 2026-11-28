package com.studiomk.pomme

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

@PreviewTest
@Preview(
    name = "PommeColorScheme Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 480,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeColorSchemeLightScreenshotTest() {
    PommeColorSchemeLightPreview()
}

@PreviewTest
@Preview(
    name = "PommeColorScheme Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 480,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeColorSchemeDarkScreenshotTest() {
    PommeColorSchemeDarkPreview()
}
