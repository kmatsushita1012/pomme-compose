package com.studiomk.pomme

import androidx.compose.foundation.background
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

@Composable
private fun PommeTypographyStylePreview(style: TextStyle, darkTheme: Boolean) {
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

@Preview(name = "Large Title Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyLargeTitleLightPreview() {
    PommeTypographyStylePreview(PommeTypography().largeTitle, darkTheme = false)
}

@Preview(name = "Large Title Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyLargeTitleDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().largeTitle, darkTheme = true)
}

@Preview(name = "Title Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyTitleLightPreview() {
    PommeTypographyStylePreview(PommeTypography().title, darkTheme = false)
}

@Preview(name = "Title Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyTitleDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().title, darkTheme = true)
}

@Preview(name = "Title 2 Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyTitle2LightPreview() {
    PommeTypographyStylePreview(PommeTypography().title2, darkTheme = false)
}

@Preview(name = "Title 2 Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyTitle2DarkPreview() {
    PommeTypographyStylePreview(PommeTypography().title2, darkTheme = true)
}

@Preview(name = "Title 3 Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyTitle3LightPreview() {
    PommeTypographyStylePreview(PommeTypography().title3, darkTheme = false)
}

@Preview(name = "Title 3 Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyTitle3DarkPreview() {
    PommeTypographyStylePreview(PommeTypography().title3, darkTheme = true)
}

@Preview(name = "Headline Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyHeadlineLightPreview() {
    PommeTypographyStylePreview(PommeTypography().headline, darkTheme = false)
}

@Preview(name = "Headline Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyHeadlineDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().headline, darkTheme = true)
}

@Preview(name = "Body Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyBodyLightPreview() {
    PommeTypographyStylePreview(PommeTypography().body, darkTheme = false)
}

@Preview(name = "Body Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyBodyDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().body, darkTheme = true)
}

@Preview(name = "Callout Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyCalloutLightPreview() {
    PommeTypographyStylePreview(PommeTypography().callout, darkTheme = false)
}

@Preview(name = "Callout Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyCalloutDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().callout, darkTheme = true)
}

@Preview(name = "Subheadline Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographySubheadlineLightPreview() {
    PommeTypographyStylePreview(PommeTypography().subheadline, darkTheme = false)
}

@Preview(name = "Subheadline Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographySubheadlineDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().subheadline, darkTheme = true)
}

@Preview(name = "Footnote Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyFootnoteLightPreview() {
    PommeTypographyStylePreview(PommeTypography().footnote, darkTheme = false)
}

@Preview(name = "Footnote Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyFootnoteDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().footnote, darkTheme = true)
}

@Preview(name = "Caption Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyCaptionLightPreview() {
    PommeTypographyStylePreview(PommeTypography().caption, darkTheme = false)
}

@Preview(name = "Caption Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyCaptionDarkPreview() {
    PommeTypographyStylePreview(PommeTypography().caption, darkTheme = true)
}

@Preview(name = "Caption 2 Light", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyCaption2LightPreview() {
    PommeTypographyStylePreview(PommeTypography().caption2, darkTheme = false)
}

@Preview(name = "Caption 2 Dark", showBackground = true, widthDp = 360, heightDp = 96)
@Composable
internal fun PommeTypographyCaption2DarkPreview() {
    PommeTypographyStylePreview(PommeTypography().caption2, darkTheme = true)
}
