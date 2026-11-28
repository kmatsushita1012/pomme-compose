package com.studiomk.pomme

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

class PommeColorSchemeTest {
    @Test
    fun lightProvidesStandardColors() {
        val colorScheme = PommeColorScheme.light()

        assertEquals(Color(0xFF007AFF), colorScheme.accentColor)
        assertEquals(Color.Black.copy(alpha = 0.85f), colorScheme.primary)
        assertEquals(Color.Black.copy(alpha = 0.55f), colorScheme.secondary)
        assertEquals(Color(0xFFFFFFFF), colorScheme.background)
        assertEquals(Color(0xFFF2F2F7), colorScheme.groupedBackground)
        assertEquals(Color(0xFFFFFFFF), colorScheme.secondaryGroupedBackground)
    }

    @Test
    fun darkProvidesStandardColors() {
        val colorScheme = PommeColorScheme.dark()

        assertEquals(Color(0xFF0A84FF), colorScheme.accentColor)
        assertEquals(Color.White.copy(alpha = 0.85f), colorScheme.primary)
        assertEquals(Color.White.copy(alpha = 0.55f), colorScheme.secondary)
        assertEquals(Color(0xFF000000), colorScheme.background)
        assertEquals(Color(0xFF000000), colorScheme.groupedBackground)
        assertEquals(Color(0xFF1C1C1E), colorScheme.secondaryGroupedBackground)
    }

    @Test
    fun copyOverridesEachRoleIndependently() {
        val defaults = PommeColorScheme.light()
        val replacement = Color(0xFF123456)

        val variants = listOf(
            defaults.copy(accentColor = replacement),
            defaults.copy(primary = replacement),
            defaults.copy(secondary = replacement),
            defaults.copy(background = replacement),
            defaults.copy(groupedBackground = replacement),
            defaults.copy(secondaryGroupedBackground = replacement),
        )
        val defaultColors = defaults.colors()

        variants.forEachIndexed { overriddenIndex, variant ->
            val colors = variant.colors()
            colors.forEachIndexed { colorIndex, color ->
                if (colorIndex == overriddenIndex) {
                    assertEquals(replacement, color)
                } else {
                    assertEquals(defaultColors[colorIndex], color)
                }
            }
        }
    }

    private fun PommeColorScheme.colors(): List<Color> =
        listOf(
            accentColor,
            primary,
            secondary,
            background,
            groupedBackground,
            secondaryGroupedBackground,
        )
}
