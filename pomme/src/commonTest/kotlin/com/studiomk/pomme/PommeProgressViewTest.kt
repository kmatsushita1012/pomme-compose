package com.studiomk.pomme

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class PommeProgressViewTest {
    @Test
    fun progressFractionHandlesIndeterminateAndBoundaryValues() {
        assertNull(progressFraction(null, 1f))
        assertEquals(0f, progressFraction(0f, 100f))
        assertEquals(0.6f, progressFraction(60f, 100f))
        assertEquals(1f, progressFraction(100f, 100f))
    }

    @Test
    fun progressFractionReflectsExternalValueUpdates() {
        assertEquals(0.2f, progressFraction(20f, 100f))
        assertEquals(0.8f, progressFraction(80f, 100f))
    }

    @Test
    fun progressFractionClampsOnlyTheDisplayedValue() {
        assertEquals(0f, progressFraction(-10f, 100f))
        assertEquals(1f, progressFraction(120f, 100f))
    }

    @Test
    fun progressFractionRejectsInvalidTotal() {
        assertFailsWith<IllegalArgumentException> { progressFraction(0.5f, 0f) }
        assertFailsWith<IllegalArgumentException> { progressFraction(0.5f, -1f) }
        assertFailsWith<IllegalArgumentException> { progressFraction(0.5f, Float.NaN) }
        assertFailsWith<IllegalArgumentException> { progressFraction(0.5f, Float.POSITIVE_INFINITY) }
    }

    @Test
    fun progressFractionRejectsNonFiniteValue() {
        assertFailsWith<IllegalArgumentException> { progressFraction(Float.NaN, 1f) }
        assertFailsWith<IllegalArgumentException> { progressFraction(Float.POSITIVE_INFINITY, 1f) }
        assertFailsWith<IllegalArgumentException> { progressFraction(Float.NEGATIVE_INFINITY, 1f) }
    }

    @Test
    fun automaticStyleFollowsDeterminateStateAndExplicitStylesWin() {
        assertEquals(
            PommeProgressViewStyle.Circular,
            resolveProgressViewStyle(PommeProgressViewStyle.Automatic, null),
        )
        assertEquals(
            PommeProgressViewStyle.Linear,
            resolveProgressViewStyle(PommeProgressViewStyle.Automatic, 0f),
        )
        assertEquals(
            PommeProgressViewStyle.Circular,
            resolveProgressViewStyle(PommeProgressViewStyle.Circular, 0.6f),
        )
        assertEquals(
            PommeProgressViewStyle.Linear,
            resolveProgressViewStyle(PommeProgressViewStyle.Linear, null),
        )
    }

    @Test
    fun semanticsDistinguishIndeterminateAndDeterminateProgress() {
        assertEquals(ProgressBarRangeInfo.Indeterminate, progressBarRangeInfo(null))
        assertEquals(
            ProgressBarRangeInfo(0.6f, 0f..1f),
            progressBarRangeInfo(0.6f),
        )
    }
}
