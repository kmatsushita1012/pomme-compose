package com.studiomk.pomme.sample

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.studiomk.pomme.PommeTypography

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
    PommeColorSchemeScreenshotPreview(darkTheme = false)
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
    PommeColorSchemeScreenshotPreview(darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Large Title Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyLargeTitleLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().largeTitle, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Large Title Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyLargeTitleDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().largeTitle, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Title Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyTitleLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().title, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Title Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyTitleDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().title, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Title 2 Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyTitle2LightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().title2, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Title 2 Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyTitle2DarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().title2, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Title 3 Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyTitle3LightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().title3, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Title 3 Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyTitle3DarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().title3, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Headline Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyHeadlineLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().headline, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Headline Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyHeadlineDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().headline, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Body Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyBodyLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().body, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Body Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyBodyDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().body, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Callout Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyCalloutLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().callout, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Callout Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyCalloutDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().callout, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Subheadline Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographySubheadlineLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().subheadline, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Subheadline Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographySubheadlineDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().subheadline, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Footnote Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyFootnoteLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().footnote, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Footnote Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyFootnoteDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().footnote, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Caption Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyCaptionLightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().caption, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Caption Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyCaptionDarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().caption, darkTheme = true)
}

@PreviewTest
@Preview(
    name = "Caption 2 Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
fun PommeTypographyCaption2LightScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().caption2, darkTheme = false)
}

@PreviewTest
@Preview(
    name = "Caption 2 Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 96,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun PommeTypographyCaption2DarkScreenshotTest() {
    PommeTypographyScreenshotPreview(PommeTypography().caption2, darkTheme = true)
}
