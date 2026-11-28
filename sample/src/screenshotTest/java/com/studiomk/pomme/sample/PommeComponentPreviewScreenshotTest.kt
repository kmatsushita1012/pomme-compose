package com.studiomk.pomme.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.studiomk.pomme.PommeTypography

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 480,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 480,
)
internal annotation class PommeColorSchemeScreenshotPreviews

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
)
internal annotation class PommeTypographyScreenshotPreviews

@PreviewTest
@PommeColorSchemeScreenshotPreviews
@Composable
fun PommeColorSchemeScreenshotTest() {
    PommeColorSchemeScreenshotFixture()
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyLargeTitleScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().largeTitle)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyTitleScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().title)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyTitle2ScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().title2)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyTitle3ScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().title3)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyHeadlineScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().headline)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyBodyScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().body)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyCalloutScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().callout)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographySubheadlineScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().subheadline)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyFootnoteScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().footnote)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyCaptionScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().caption)
}

@PreviewTest
@PommeTypographyScreenshotPreviews
@Composable
fun PommeTypographyCaption2ScreenshotTest() {
    PommeTypographyScreenshotFixture(PommeTypography().caption2)
}
