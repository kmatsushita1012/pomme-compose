package com.studiomk.pomme.sample

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.studiomk.pomme.PommeColorScheme
import com.studiomk.pomme.PommeLazyRowItem
import com.studiomk.pomme.PommeLazyRows
import com.studiomk.pomme.PommeRowMetrics
import com.studiomk.pomme.PommeRowsLayout
import com.studiomk.pomme.PommeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PommeRowsLayoutTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun directLayoutNodesAreRowsAndStandardLayoutsGroupTheirChildren() {
        val showOptionalRow = mutableStateOf(false)
        composeRule.setContent {
            PommeTestTheme {
                PommeRowsLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rows"),
                    metrics = testMetrics,
                    surfaceColor = Color.White,
                ) {
                    Text("First direct node")
                    if (showOptionalRow.value) Text("Conditional direct node")
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("Grouped left")
                        Text("Grouped right")
                    }
                }
                PommeRowsLayout(
                    modifier = Modifier.testTag("empty-rows"),
                    metrics = testMetrics,
                    surfaceColor = null,
                ) {}
            }
        }

        composeRule.onNodeWithText("First direct node").assertExists()
        composeRule.onNodeWithText("Grouped left").assertExists()
        composeRule.onNodeWithText("Grouped right").assertExists()
        composeRule.onNodeWithText("Conditional direct node").assertDoesNotExist()
        composeRule.onNodeWithTag("rows").assertHeightIsEqualTo(88.dp)
        composeRule.onNodeWithTag("empty-rows").assertHeightIsEqualTo(0.dp)

        composeRule.runOnIdle { showOptionalRow.value = true }

        composeRule.onNodeWithText("Conditional direct node").assertExists()
        composeRule.onNodeWithTag("rows").assertHeightIsAtLeast(132.dp)
    }

    @Test
    fun focusedInputAndValueSurviveConditionalInsertion() {
        val showOptionalRow = mutableStateOf(false)
        val editorValue = mutableStateOf("")
        composeRule.setContent {
            PommeTestTheme {
                PommeRowsLayout(
                    modifier = Modifier.fillMaxWidth(),
                    metrics = testMetrics,
                    surfaceColor = Color.White,
                ) {
                    if (showOptionalRow.value) Text("Inserted row")
                    key("editor") {
                        BasicTextField(
                            value = editorValue.value,
                            onValueChange = { editorValue.value = it },
                            modifier = Modifier.testTag("editor"),
                        )
                    }
                }
            }
        }

        composeRule.onNodeWithTag("editor").performClick()
        composeRule.onNodeWithTag("editor").performTextInput("kept")
        composeRule.runOnIdle { showOptionalRow.value = true }

        composeRule.onNodeWithTag("editor").assertIsFocused()
        composeRule.onNodeWithTag("editor").assertTextEquals("kept")
    }

    @Test
    fun lazyRowsRenderVisibleItemsAndKeepKeyedStateAfterInsertion() {
        val rows = mutableStateOf((0 until 500).toList())
        composeRule.setContent {
            PommeTestTheme {
                PommeLazyRows(
                    items = rows.value.map { index ->
                        PommeLazyRowItem(key = index) {
                            if (index == 0) {
                                val value = remember { mutableStateOf("Row 0") }
                                BasicTextField(
                                    value = value.value,
                                    onValueChange = { value.value = it },
                                    modifier = Modifier.testTag("lazy-editor"),
                                )
                            } else {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text("Row $index")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .testTag("lazy-rows"),
                    metrics = testMetrics,
                    surfaceColor = Color.White,
                )
            }
        }

        composeRule.onNodeWithTag("lazy-editor").performClick()
        composeRule.onNodeWithTag("lazy-editor").performTextInput(" kept")
        composeRule.onNodeWithTag("lazy-editor").assertTextEquals("Row 0 kept")
        composeRule.runOnIdle { rows.value = listOf(-1) + rows.value }
        composeRule.onNodeWithTag("lazy-editor").assertTextEquals("Row 0 kept")
        composeRule.onNodeWithText("Row 499").assertDoesNotExist()
        composeRule.onNodeWithTag("lazy-rows").performScrollToNode(hasText("Row 499"))
        composeRule.onNodeWithText("Row 499").assertExists()
    }
}

@Composable
private fun PommeTestTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        PommeTheme(colorScheme = PommeColorScheme.light(), content = content)
    }
}

private val testMetrics = PommeRowMetrics(
    horizontalInset = 16.dp,
    verticalPadding = 8.dp,
    minimumHeight = 44.dp,
    dividerInset = 16.dp,
    dividerThickness = 0.5.dp,
    dividerColor = Color.Gray,
)
