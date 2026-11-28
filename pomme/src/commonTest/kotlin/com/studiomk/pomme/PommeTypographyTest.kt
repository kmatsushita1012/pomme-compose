package com.studiomk.pomme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PommeTypographyTest {
    @Test
    fun defaultStylesMatchApprovedValuesAndKeepOtherAttributesUnspecified() {
        val typography = PommeTypography()

        assertEquals(TextStyle(fontSize = 34.sp, lineHeight = 41.sp, fontWeight = FontWeight.Normal), typography.largeTitle)
        assertEquals(TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.Normal), typography.title)
        assertEquals(TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Normal), typography.title2)
        assertEquals(TextStyle(fontSize = 20.sp, lineHeight = 25.sp, fontWeight = FontWeight.Normal), typography.title3)
        assertEquals(TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.SemiBold), typography.headline)
        assertEquals(TextStyle(fontSize = 17.sp, lineHeight = 22.sp, fontWeight = FontWeight.Normal), typography.body)
        assertEquals(TextStyle(fontSize = 16.sp, lineHeight = 21.sp, fontWeight = FontWeight.Normal), typography.callout)
        assertEquals(TextStyle(fontSize = 15.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal), typography.subheadline)
        assertEquals(TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Normal), typography.footnote)
        assertEquals(TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal), typography.caption)
        assertEquals(TextStyle(fontSize = 11.sp, lineHeight = 13.sp, fontWeight = FontWeight.Normal), typography.caption2)
    }

    @Test
    fun eachNamedArgumentReplacesOnlyItsMatchingStyle() {
        val default = PommeTypography()
        val replacement = TextStyle(fontSize = 99.sp, lineHeight = 101.sp, fontWeight = FontWeight.Bold)

        assertEquals(default.copy(largeTitle = replacement), PommeTypography(largeTitle = replacement))
        assertEquals(default.copy(title = replacement), PommeTypography(title = replacement))
        assertEquals(default.copy(title2 = replacement), PommeTypography(title2 = replacement))
        assertEquals(default.copy(title3 = replacement), PommeTypography(title3 = replacement))
        assertEquals(default.copy(headline = replacement), PommeTypography(headline = replacement))
        assertEquals(default.copy(body = replacement), PommeTypography(body = replacement))
        assertEquals(default.copy(callout = replacement), PommeTypography(callout = replacement))
        assertEquals(default.copy(subheadline = replacement), PommeTypography(subheadline = replacement))
        assertEquals(default.copy(footnote = replacement), PommeTypography(footnote = replacement))
        assertEquals(default.copy(caption = replacement), PommeTypography(caption = replacement))
        assertEquals(default.copy(caption2 = replacement), PommeTypography(caption2 = replacement))
    }

    @Test
    fun copyReplacesOneStyleWithoutMutatingTheOriginal() {
        val typography = PommeTypography()
        val replacement = TextStyle(fontSize = 18.sp, lineHeight = 24.sp, fontWeight = FontWeight.Bold)

        val customized = typography.copy(body = replacement)

        assertEquals(replacement, customized.body)
        assertEquals(17.sp, typography.body.fontSize)
        assertEquals(typography.copy(body = replacement), customized)
    }

    @Test
    fun textSizeUsesAndroidFontScale() {
        val fontSize = PommeTypography().body.fontSize

        val normalScalePx = with(Density(density = 1f, fontScale = 1f)) { fontSize.toPx() }
        val enlargedScalePx = with(Density(density = 1f, fontScale = 1.5f)) { fontSize.toPx() }

        assertEquals(TextUnitType.Sp, fontSize.type)
        assertEquals(17f, normalScalePx, 0f)
        assertTrue(enlargedScalePx > normalScalePx)
    }
}
