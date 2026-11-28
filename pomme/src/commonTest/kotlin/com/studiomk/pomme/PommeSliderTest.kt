package com.studiomk.pomme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PommeSliderTest {
    @Test
    fun validRangeAndStepAreAccepted() {
        validatePommeSliderArguments(0f..1f, step = null)
        validatePommeSliderArguments(-2f..4f, step = 0.25f)
        validatePommeSliderArguments(0f..0.5f, step = 1f)
    }

    @Test
    fun invalidRangesAreRejected() {
        assertFailsWith<IllegalArgumentException> {
            validatePommeSliderArguments(1f..1f, step = null)
        }
        assertFailsWith<IllegalArgumentException> {
            validatePommeSliderArguments(2f..1f, step = null)
        }
        assertFailsWith<IllegalArgumentException> {
            validatePommeSliderArguments(Float.NaN..1f, step = null)
        }
        assertFailsWith<IllegalArgumentException> {
            validatePommeSliderArguments(Float.NEGATIVE_INFINITY..1f, step = null)
        }
        assertFailsWith<IllegalArgumentException> {
            validatePommeSliderArguments(0f..Float.POSITIVE_INFINITY, step = null)
        }
    }

    @Test
    fun invalidStepsAreRejected() {
        listOf(0f, -0.1f, Float.NaN, Float.POSITIVE_INFINITY).forEach { step ->
            assertFailsWith<IllegalArgumentException> {
                validatePommeSliderArguments(0f..1f, step)
            }
        }
    }

    @Test
    fun displayedValueIsClampedWithoutQuantizing() {
        assertEquals(0f, clampPommeSliderValue(-1f, 0f..1f))
        assertEquals(1f, clampPommeSliderValue(2f, 0f..1f))
        assertEquals(0.2f, clampPommeSliderValue(0.2f, 0f..1f))
        assertEquals(0f, clampPommeSliderValue(Float.NaN, 0f..1f))
        assertEquals(0f, clampPommeSliderValue(Float.NEGATIVE_INFINITY, 0f..1f))
        assertEquals(1f, clampPommeSliderValue(Float.POSITIVE_INFINITY, 0f..1f))
    }

    @Test
    fun continuousValueClampsToRange() {
        assertEquals(0f, snapPommeSliderValue(-1f, 0f..1f, step = null))
        assertEquals(0.37f, snapPommeSliderValue(0.37f, 0f..1f, step = null))
        assertEquals(1f, snapPommeSliderValue(2f, 0f..1f, step = null))
    }

    @Test
    fun steppedValueUsesLowerBoundAsOrigin() {
        assertEquals(0f, snapPommeSliderValue(0.04f, 0f..1f, step = 0.3f))
        assertEquals(0.3f, snapPommeSliderValue(0.16f, 0f..1f, step = 0.3f))
        assertEquals(0.6f, snapPommeSliderValue(0.58f, 0f..1f, step = 0.3f))
        assertEquals(0.9f, snapPommeSliderValue(0.94f, 0f..1f, step = 0.3f), 0.0001f)
        assertEquals(1f, snapPommeSliderValue(0.96f, 0f..1f, step = 0.3f))
    }

    @Test
    fun steppedValueKeepsBothEndpointsReachableForNonDivisibleRanges() {
        assertEquals(0f, snapPommeSliderValue(0f, 0f..1f, step = 0.3f))
        assertEquals(1f, snapPommeSliderValue(1f, 0f..1f, step = 0.3f))
        assertEquals(2f, snapPommeSliderValue(2f, 0.5f..2f, step = 0.4f))
        assertEquals(1.7f, snapPommeSliderValue(1.54f, 0.5f..2f, step = 0.4f))
    }

    @Test
    fun steppedValueHandlesFloatingPointRounding() {
        assertEquals(0.3f, snapPommeSliderValue(0.30000004f, 0f..1f, step = 0.1f))
    }

    @Test
    fun semanticStepCountMatchesSelectableIntermediateStops() {
        assertEquals(0, pommeSliderSemanticSteps(0f..1f, step = null))
        assertEquals(3, pommeSliderSemanticSteps(0f..1f, step = 0.25f))
        assertEquals(3, pommeSliderSemanticSteps(0f..1f, step = 0.3f))
        assertEquals(0, pommeSliderSemanticSteps(0f..0.5f, step = 1f))
    }
}
