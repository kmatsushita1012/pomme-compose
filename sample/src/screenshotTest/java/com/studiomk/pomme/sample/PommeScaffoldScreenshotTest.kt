package com.studiomk.pomme.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.studiomk.pomme.PommeColorScheme
import com.studiomk.pomme.PommeSafeAreaEdge
import com.studiomk.pomme.PommeScaffold
import com.studiomk.pomme.PommeTheme
import com.studiomk.pomme.PommeToolbarPlacement

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
internal annotation class PommeScaffoldScreenshotPreviews

@PreviewTest
@PommeScaffoldScreenshotPreviews
@Composable
fun PommeScaffoldFloatingToolbarScreenshotTest() {
    PommeScaffoldScreenshotTheme {
        PommeScaffold {
            navigationTitle("アカウント設定")
            content { PommeScaffoldScreenshotContent("Toolbar はコンテンツ上に浮かびます") }
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                    Text("戻る", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {
                    Text("編集", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.BottomBar) {
                    Text("場所を表示", color = PommeTheme.colorScheme.accentColor)
                }
            }
        }
    }
}

@PreviewTest
@PommeScaffoldScreenshotPreviews
@Composable
fun PommeScaffoldSafeAreaScreenshotTest() {
    PommeScaffoldScreenshotTheme {
        PommeScaffold {
            navigationTitle("安全領域あり")
            safeArea(PommeSafeAreaEdge.Top, PommeSafeAreaEdge.Bottom)
            toolbarBackgroundVisibility(top = true, bottom = false)
            content { PommeScaffoldScreenshotContent("Toolbar の内側からコンテンツを開始") }
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                    Text("戻る", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.BottomBar) {
                    Text("完了", color = PommeTheme.colorScheme.accentColor)
                }
            }
        }
    }
}

@PreviewTest
@PommeScaffoldScreenshotPreviews
@Composable
fun PommeScaffoldNoToolbarScreenshotTest() {
    PommeScaffoldScreenshotTheme {
        PommeScaffold {
            safeArea(PommeSafeAreaEdge.Top, PommeSafeAreaEdge.Bottom)
            content { PommeScaffoldScreenshotContent("Toolbar がない画面") }
        }
    }
}

@PreviewTest
@PommeScaffoldScreenshotPreviews
@Composable
fun PommeScaffoldToolbarBackgroundVisibilityScreenshotTest() {
    PommeScaffoldScreenshotTheme {
        PommeScaffold {
            toolbarBackgroundVisibility(top = false, bottom = true)
            content { PommeScaffoldScreenshotContent("下側だけグラデーション") }
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                    Text("戻る", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.BottomBar) {
                    Text("ツールバー", color = PommeTheme.colorScheme.accentColor)
                }
            }
        }
    }
}

@PreviewTest
@PommeScaffoldScreenshotPreviews
@Composable
fun PommeScaffoldPrincipalScreenshotTest() {
    PommeScaffoldScreenshotTheme {
        PommeScaffold {
            navigationTitle("Navigation title")
            content { PommeScaffoldScreenshotContent("Principal item が navigationTitle を置き換える") }
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                    Text("戻る", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.Principal) {
                    Text("Principal", color = PommeTheme.colorScheme.primary)
                }
                PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {
                    Text("保存", color = PommeTheme.colorScheme.accentColor)
                }
            }
        }
    }
}

@PreviewTest
@PommeScaffoldScreenshotPreviews
@Composable
fun PommeScaffoldLongTitleScreenshotTest() {
    PommeScaffoldScreenshotTheme {
        PommeScaffold {
            navigationTitle("複数の Toolbar Item と競合する長いナビゲーションタイトル")
            content { PommeScaffoldScreenshotContent("タイトルは一行に省略") }
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                    Text("キャンセル", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                    Text("追加", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {
                    Text("検索", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {
                    Text("保存", color = PommeTheme.colorScheme.accentColor)
                }
            }
        }
    }
}

@PreviewTest
@PommeScaffoldScreenshotPreviews
@Composable
fun PommeScaffoldRtlScreenshotTest() {
    PommeScaffoldScreenshotTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            PommeScaffold {
                navigationTitle("RTL レイアウト")
                content { PommeScaffoldScreenshotContent("Leading / Trailing は論理方向に配置") }
                toolbar {
                    PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                        Text("Leading", color = PommeTheme.colorScheme.accentColor)
                    }
                    PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {
                        Text("Trailing", color = PommeTheme.colorScheme.accentColor)
                    }
                }
            }
        }
    }
}

@Composable
private fun PommeScaffoldScreenshotTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme {
        PommeTheme(
            colorScheme = if (darkTheme) PommeColorScheme.dark() else PommeColorScheme.light(),
            content = content,
        )
    }
}

@Composable
private fun PommeScaffoldScreenshotContent(text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PommeTheme.colorScheme.groupedBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("コンテンツ先頭", color = PommeTheme.colorScheme.secondary)
            Text(text = text, color = PommeTheme.colorScheme.primary)
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("コンテンツ末尾", color = PommeTheme.colorScheme.primary)
            Text("システム安全領域と Toolbar の位置を親が管理", color = PommeTheme.colorScheme.secondary)
        }
    }
}
