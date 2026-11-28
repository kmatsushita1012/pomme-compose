package com.studiomk.pomme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The standard SwiftUI-aligned text styles used by Pomme components.
 *
 * Each default style sets only its font size, line height, and weight. Other attributes keep
 * their Compose defaults, and sizes use [sp] so Android's font scale is respected. Pass a
 * named argument or use [copy] to replace a style; replacement values are not merged or
 * adjusted implicitly.
 *
 * @property largeTitle Large title style, defaulting to 34sp with a 41sp line height.
 * @property title First-level title style, defaulting to 28sp with a 34sp line height.
 * @property title2 Second-level title style, defaulting to 22sp with a 28sp line height.
 * @property title3 Third-level title style, defaulting to 20sp with a 25sp line height.
 * @property headline Headline style, defaulting to semibold 17sp with a 22sp line height.
 * @property body Body style, defaulting to 17sp with a 22sp line height.
 * @property callout Callout style, defaulting to 16sp with a 21sp line height.
 * @property subheadline Subheadline style, defaulting to 15sp with a 20sp line height.
 * @property footnote Footnote style, defaulting to 13sp with an 18sp line height.
 * @property caption Caption style, defaulting to 12sp with a 16sp line height.
 * @property caption2 Alternate caption style, defaulting to 11sp with a 13sp line height.
 */
@Immutable
data class PommeTypography(
    val largeTitle: TextStyle = TextStyle(fontSize = 34.sp, lineHeight = 41.sp, fontWeight = FontWeight.Normal),
    val title: TextStyle = TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Normal),
    val title2: TextStyle = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Normal),
    val title3: TextStyle = TextStyle(fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.Normal),
    val headline: TextStyle = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold),
    val body: TextStyle = TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.Normal),
    val callout: TextStyle = TextStyle(fontSize = 16.sp, lineHeight = 21.sp, fontWeight = FontWeight.Normal),
    val subheadline: TextStyle = TextStyle(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal),
    val footnote: TextStyle = TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Normal),
    val caption: TextStyle = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
    val caption2: TextStyle = TextStyle(fontSize = 11.sp, lineHeight = 13.sp, fontWeight = FontWeight.Normal)
)

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    widthDp = 360,
    heightDp = 96,
    showBackground = true,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    widthDp = 360,
    heightDp = 96,
    showBackground = true,
)
internal annotation class PommeTypographyLightDarkPreview

@Composable
private fun PommeTypographyStylePreview(style: TextStyle) {
    val darkTheme = isSystemInDarkTheme()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (darkTheme) Color(0xFF121212) else Color.White)
            .padding(24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicText(
            text = "Sample text",
            style = style.copy(
                color = if (darkTheme) Color(0xFFE6E1E5) else Color(0xFF1B1B1B)
            )
        )
    }
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyLargeTitlePreview() {
    PommeTypographyStylePreview(PommeTypography().largeTitle)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyTitlePreview() {
    PommeTypographyStylePreview(PommeTypography().title)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyTitle2Preview() {
    PommeTypographyStylePreview(PommeTypography().title2)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyTitle3Preview() {
    PommeTypographyStylePreview(PommeTypography().title3)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyHeadlinePreview() {
    PommeTypographyStylePreview(PommeTypography().headline)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyBodyPreview() {
    PommeTypographyStylePreview(PommeTypography().body)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyCalloutPreview() {
    PommeTypographyStylePreview(PommeTypography().callout)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographySubheadlinePreview() {
    PommeTypographyStylePreview(PommeTypography().subheadline)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyFootnotePreview() {
    PommeTypographyStylePreview(PommeTypography().footnote)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyCaptionPreview() {
    PommeTypographyStylePreview(PommeTypography().caption)
}

@PommeTypographyLightDarkPreview
@Composable
internal fun PommeTypographyCaption2Preview() {
    PommeTypographyStylePreview(PommeTypography().caption2)
}
