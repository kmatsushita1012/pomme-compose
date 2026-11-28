package com.studiomk.pomme.sample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.down
import androidx.compose.ui.test.moveTo
import androidx.compose.ui.test.up
import androidx.compose.ui.test.cancel
import androidx.compose.ui.test.click
import androidx.compose.ui.test.SemanticsMatcher
import com.studiomk.pomme.PommeSlider
import com.studiomk.pomme.PommeTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PommeSliderTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun accessibilitySetProgressUpdatesValueAndEmitsOneEditingSession() {
        var value by mutableFloatStateOf(0.2f)
        val editingEvents = mutableStateListOf<Boolean>()

        composeRule.setContent {
            PommeTheme {
                PommeSlider(
                    value = value,
                    onValueChange = { value = it },
                    onEditingChanged = { editingEvents.add(it) },
                )
            }
        }

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))
            .performSemanticsAction(SemanticsActions.SetProgress) {
            it(0.8f)
        }

        composeRule.runOnIdle {
            assertEquals(0.8f, value, 0.0001f)
            assertEquals(listOf(true, false), editingEvents)
        }
    }

    @Test
    fun tapChangesValueAndFinishesTheEditingSession() {
        var value by mutableFloatStateOf(0.5f)
        val editingEvents = mutableStateListOf<Boolean>()

        composeRule.setContent {
            PommeTheme {
                PommeSlider(
                    value = value,
                    onValueChange = { value = it },
                    onEditingChanged = { editingEvents.add(it) },
                )
            }
        }

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress)).performTouchInput {
            click(Offset(x = 20f, y = center.y))
        }

        composeRule.runOnIdle {
            assertEquals(0f, value, 0.0001f)
            assertEquals(listOf(true, false), editingEvents)
        }
    }

    @Test
    fun dragEmitsOneStartAndOneEndAcrossMultipleValueChanges() {
        var value by mutableFloatStateOf(0.2f)
        val editingEvents = mutableStateListOf<Boolean>()

        composeRule.setContent {
            PommeTheme {
                PommeSlider(
                    value = value,
                    onValueChange = { value = it },
                    onEditingChanged = { editingEvents.add(it) },
                )
            }
        }

        val slider = composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))
        val width = slider.fetchSemanticsNode().boundsInRoot.width
        slider.performTouchInput {
            down(Offset(x = width * 0.1f, y = center.y))
            moveTo(Offset(x = width * 0.45f, y = center.y), delayMillis = 100)
            moveTo(Offset(x = width * 0.8f, y = center.y), delayMillis = 100)
            up()
        }

        composeRule.runOnIdle {
            assertTrue(value > 0.5f)
            assertEquals(listOf(true, false), editingEvents)
        }
    }

    @Test
    fun cancelledDragEndsTheEditingSession() {
        val editingEvents = mutableStateListOf<Boolean>()

        composeRule.setContent {
            PommeTheme {
                PommeSlider(
                    value = 0.2f,
                    onValueChange = {},
                    onEditingChanged = { editingEvents.add(it) },
                )
            }
        }

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress)).performTouchInput {
            down(Offset(x = 24f, y = center.y))
            moveTo(Offset(x = 100f, y = center.y), delayMillis = 100)
            cancel()
        }

        composeRule.runOnIdle {
            assertEquals(listOf(true, false), editingEvents)
        }
    }

    @Test
    fun externalValueUpdateDoesNotCallChangeOrEditingCallbacks() {
        var value by mutableFloatStateOf(0.2f)
        var changeCalls = 0
        val editingEvents = mutableStateListOf<Boolean>()

        composeRule.setContent {
            PommeTheme {
                PommeSlider(
                    value = value,
                    onValueChange = { changeCalls += 1 },
                    onEditingChanged = { editingEvents.add(it) },
                )
            }
        }

        composeRule.runOnIdle { value = 0.8f }

        composeRule.runOnIdle {
            assertEquals(0, changeCalls)
            assertTrue(editingEvents.isEmpty())
        }
        val progressNode = composeRule.onNode(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo),
        ).fetchSemanticsNode()
        assertEquals(0.8f, progressNode.config[SemanticsProperties.ProgressBarRangeInfo].current, 0.0001f)
    }

    @Test
    fun outOfRangeValueIsClampedForDisplayWithoutWritingBack() {
        var value by mutableFloatStateOf(2f)
        var changeCalls = 0

        composeRule.setContent {
            PommeTheme {
                PommeSlider(value = value, onValueChange = { changeCalls += 1 })
            }
        }

        val progressNode = composeRule.onNode(
            SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo),
        ).fetchSemanticsNode()

        assertEquals(1f, progressNode.config[SemanticsProperties.ProgressBarRangeInfo].current, 0.0001f)
        composeRule.runOnIdle {
            assertEquals(2f, value)
            assertEquals(0, changeCalls)
        }
    }

    @Test
    fun steppedAccessibilityAdjustmentCanReachTheUpperBound() {
        var value by mutableFloatStateOf(0.9f)

        composeRule.setContent {
            PommeTheme {
                PommeSlider(
                    value = value,
                    onValueChange = { value = it },
                    valueRange = 0f..1f,
                    step = 0.3f,
                )
            }
        }

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress))
            .performSemanticsAction(SemanticsActions.SetProgress) {
                it(1f)
            }

        composeRule.runOnIdle { assertEquals(1f, value, 0.0001f) }
    }

    @Test
    fun tappingThePhysicalLeftSideSelectsMaximumInRtl() {
        var value by mutableFloatStateOf(0.5f)

        composeRule.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                PommeTheme {
                    PommeSlider(value = value, onValueChange = { value = it })
                }
            }
        }

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress)).performTouchInput {
            click(Offset(x = 20f, y = center.y))
        }

        composeRule.runOnIdle { assertEquals(1f, value, 0.0001f) }
    }

    @Test
    fun disabledSliderHasNoRangeAdjustmentAction() {
        composeRule.setContent {
            PommeTheme {
                PommeSlider(value = 0.4f, onValueChange = {}, enabled = false)
            }
        }

        assertTrue(
            composeRule.onAllNodes(
                SemanticsMatcher.keyIsDefined(SemanticsActions.SetProgress),
            ).fetchSemanticsNodes().isEmpty(),
        )
    }
}
