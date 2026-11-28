package com.studiomk.pomme

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/** Marks the receiver scopes used by Pomme's declarative UI builders. */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
public annotation class PommeDsl

/** A toolbar item location supported by [PommeScaffold]. */
public enum class PommeToolbarPlacement {
    /** Places items at the logical leading edge of the top toolbar. */
    TopBarLeading,

    /** Places items at the logical trailing edge of the top toolbar. */
    TopBarTrailing,

    /** Places custom content in the center of the top toolbar. */
    Principal,

    /** Places items in the bottom toolbar. */
    BottomBar,
}

/** A screen edge whose safe drawing inset and floating toolbar area are reserved for content. */
public enum class PommeSafeAreaEdge {
    /** Reserves the top system inset and the top toolbar's measured height. */
    Top,

    /** Reserves the bottom system inset and the bottom toolbar's measured height. */
    Bottom,
}

/** Scope for declaring the structure and screen-level settings of [PommeScaffold]. */
@PommeDsl
public interface PommeScaffoldScope {
    /** Sets the one-line title shown in the center when no principal toolbar item is declared. */
    public fun navigationTitle(title: String)

    /** Declares the screen content. The content is drawn beneath floating toolbar items. */
    public fun content(content: @Composable () -> Unit)

    /** Declares toolbar items and their explicit placements. */
    public fun toolbar(content: PommeToolbarScope.() -> Unit)

    /** Reserves the requested system-safe edges and the measured toolbar areas for [content]. */
    public fun safeArea(vararg edges: PommeSafeAreaEdge)

    /** Independently controls the fading background behind the top and bottom toolbar items. */
    public fun toolbarBackgroundVisibility(top: Boolean, bottom: Boolean)
}

/** Scope for declaring items inside [PommeScaffoldScope.toolbar]. */
@PommeDsl
public interface PommeToolbarScope {
    /** Adds a composable toolbar item at [placement], preserving declaration order. */
    public fun PommeToolbarItem(
        placement: PommeToolbarPlacement,
        content: @Composable () -> Unit,
    )
}

/**
 * Hosts full-screen content with floating top and bottom toolbar items.
 *
 * The screen content is edge-to-edge unless [PommeScaffoldScope.safeArea] reserves an edge. The
 * toolbar always respects system-safe drawing insets, including the IME at the bottom. A
 * navigation title is centered and truncated to one line; a [PommeToolbarPlacement.Principal]
 * item takes precedence over that title. Toolbar item groups preserve declaration order and can
 * scroll horizontally when their available width is limited. No navigation or action behavior is
 * generated.
 *
 * @param modifier Modifier applied to the screen host.
 * @param content Declarations for the screen content, title, toolbar, safe area, and toolbar
 *   background visibility.
 */
@Composable
public fun PommeScaffold(
    modifier: Modifier = Modifier,
    content: PommeScaffoldScope.() -> Unit,
) {
    val scope = PommeScaffoldScopeState().apply(content)
    val declaration = scope.toDeclaration()
    val safeDrawingInsets = PommeScaffoldSafeDrawingInsets()
    val topInsets = safeDrawingInsets.only(WindowInsetsSides.Top)
    val bottomInsets = safeDrawingInsets.only(WindowInsetsSides.Bottom)

    var topToolbarHeightPx by remember { mutableIntStateOf(0) }
    var bottomToolbarHeightPx by remember { mutableIntStateOf(0) }

    val density = androidx.compose.ui.platform.LocalDensity.current
    val topToolbarHeight = with(density) { topToolbarHeightPx.toDp() }
    val bottomToolbarHeight = with(density) { bottomToolbarHeightPx.toDp() }
    val reserveTopToolbar = declaration.safeAreaEdges.contains(PommeSafeAreaEdge.Top)
    val reserveBottomToolbar = declaration.safeAreaEdges.contains(PommeSafeAreaEdge.Bottom)
    val showTopBackground = shouldShowToolbarBackground(
        explicitVisibility = declaration.toolbarBackgroundVisibility?.top,
        hasItems = declaration.hasTopToolbarContent,
    )
    val showBottomBackground = shouldShowToolbarBackground(
        explicitVisibility = declaration.toolbarBackgroundVisibility?.bottom,
        hasItems = declaration.hasBottomToolbarContent,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PommeTheme.colorScheme.background),
    ) {
        val contentModifier = Modifier
            .fillMaxSize()
            .then(if (reserveTopToolbar) Modifier.windowInsetsPadding(topInsets) else Modifier)
            .padding(
                top = with(density) {
                    reservedToolbarHeightPx(reserveTopToolbar, topToolbarHeightPx).toDp()
                },
            )
            .then(if (reserveBottomToolbar) Modifier.windowInsetsPadding(bottomInsets) else Modifier)
            .padding(
                bottom = with(density) {
                    reservedToolbarHeightPx(reserveBottomToolbar, bottomToolbarHeightPx).toDp()
                },
            )
        Box(modifier = contentModifier) {
            declaration.content?.invoke()
        }

        if (showTopBackground) {
            PommeToolbarBackground(
                isTop = true,
                contentHeight = topToolbarHeight,
                insets = topInsets,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
        if (showBottomBackground) {
            PommeToolbarBackground(
                isTop = false,
                contentHeight = bottomToolbarHeight,
                insets = bottomInsets,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .windowInsetsPadding(topInsets),
        ) {
            PommeTopToolbar(
                title = declaration.navigationTitle.takeUnless { declaration.hasPrincipalItem },
                items = declaration.toolbarItems,
                onHeightChanged = { topToolbarHeightPx = it },
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .windowInsetsPadding(bottomInsets),
        ) {
            PommeBottomToolbar(
                items = declaration.toolbarItems.filter {
                    it.placement == PommeToolbarPlacement.BottomBar
                },
                onHeightChanged = { bottomToolbarHeightPx = it },
            )
        }
    }
}

internal val LocalPommeToolbarContext = compositionLocalOf { false }

@Composable
internal expect fun PommeScaffoldSafeDrawingInsets(): WindowInsets

internal data class PommeToolbarItemDeclaration(
    val placement: PommeToolbarPlacement,
    val content: @Composable () -> Unit,
)

internal data class PommeToolbarBackgroundVisibility(
    val top: Boolean,
    val bottom: Boolean,
)

internal data class PommeScaffoldDeclaration(
    val navigationTitle: String?,
    val content: (@Composable () -> Unit)?,
    val toolbarItems: List<PommeToolbarItemDeclaration>,
    val safeAreaEdges: Set<PommeSafeAreaEdge>,
    val toolbarBackgroundVisibility: PommeToolbarBackgroundVisibility?,
) {
    val hasPrincipalItem: Boolean
        get() = toolbarItems.any { it.placement == PommeToolbarPlacement.Principal }

    val hasTopToolbarContent: Boolean
        get() = navigationTitle != null || toolbarItems.any {
            it.placement != PommeToolbarPlacement.BottomBar
        }

    val hasBottomToolbarContent: Boolean
        get() = toolbarItems.any { it.placement == PommeToolbarPlacement.BottomBar }
}

internal class PommeScaffoldScopeState : PommeScaffoldScope {
    private var title: String? = null
    private var screenContent: (@Composable () -> Unit)? = null
    private val items = mutableListOf<PommeToolbarItemDeclaration>()
    private var safeEdges: Set<PommeSafeAreaEdge> = emptySet()
    private var backgroundVisibility: PommeToolbarBackgroundVisibility? = null

    override fun navigationTitle(title: String) {
        this.title = title
    }

    override fun content(content: @Composable () -> Unit) {
        screenContent = content
    }

    override fun toolbar(content: PommeToolbarScope.() -> Unit) {
        PommeToolbarScopeState(items).content()
    }

    override fun safeArea(vararg edges: PommeSafeAreaEdge) {
        safeEdges = edges.toSet()
    }

    override fun toolbarBackgroundVisibility(top: Boolean, bottom: Boolean) {
        backgroundVisibility = PommeToolbarBackgroundVisibility(top, bottom)
    }

    fun toDeclaration(): PommeScaffoldDeclaration = PommeScaffoldDeclaration(
        navigationTitle = title,
        content = screenContent,
        toolbarItems = items.toList(),
        safeAreaEdges = safeEdges,
        toolbarBackgroundVisibility = backgroundVisibility,
    )
}

private class PommeToolbarScopeState(
    private val items: MutableList<PommeToolbarItemDeclaration>,
) : PommeToolbarScope {
    override fun PommeToolbarItem(
        placement: PommeToolbarPlacement,
        content: @Composable () -> Unit,
    ) {
        items += PommeToolbarItemDeclaration(placement, content)
    }
}

internal fun shouldShowToolbarBackground(explicitVisibility: Boolean?, hasItems: Boolean): Boolean =
    explicitVisibility ?: hasItems

internal fun reservedToolbarHeightPx(safeAreaRequested: Boolean, toolbarHeightPx: Int): Int =
    if (safeAreaRequested) toolbarHeightPx.coerceAtLeast(0) else 0

private enum class PommeTopToolbarSlot {
    Leading,
    Trailing,
    Center,
}

@Composable
private fun PommeTopToolbar(
    title: String?,
    items: List<PommeToolbarItemDeclaration>,
    onHeightChanged: (Int) -> Unit,
) {
    val leadingItems = items.filter { it.placement == PommeToolbarPlacement.TopBarLeading }
    val trailingItems = items.filter { it.placement == PommeToolbarPlacement.TopBarTrailing }
    val principalItems = items.filter { it.placement == PommeToolbarPlacement.Principal }
    SubcomposeLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onSizeChanged { onHeightChanged(it.height) },
    ) { constraints ->
        val screenWidth = constraints.maxWidth
        val sideMaxWidth = screenWidth / 2
        val sideConstraints = Constraints(
            maxWidth = sideMaxWidth,
            maxHeight = constraints.maxHeight,
        )

        val leading = if (leadingItems.isNotEmpty()) {
            subcompose(PommeTopToolbarSlot.Leading) {
                PommeToolbarItemsRow(
                    items = leadingItems,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }.single().measure(sideConstraints)
        } else {
            null
        }
        val trailing = if (trailingItems.isNotEmpty()) {
            subcompose(PommeTopToolbarSlot.Trailing) {
                PommeToolbarItemsRow(
                    items = trailingItems,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }.single().measure(sideConstraints)
        } else {
            null
        }

        val widestSideWidth = maxOf(leading?.width ?: 0, trailing?.width ?: 0)
        val centerMaxWidth = (screenWidth - widestSideWidth * 2).coerceAtLeast(0)
        val centerConstraints = Constraints(
            maxWidth = centerMaxWidth,
            maxHeight = constraints.maxHeight,
        )
        val center = when {
            principalItems.isNotEmpty() -> subcompose(PommeTopToolbarSlot.Center) {
                PommeToolbarItemsRow(
                    items = principalItems,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }.single().measure(centerConstraints)

            title != null -> subcompose(PommeTopToolbarSlot.Center) {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = PommeTheme.typography.headline,
                    color = PommeTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }.single().measure(centerConstraints)

            else -> null
        }

        val contentHeight = maxOf(
            leading?.height ?: 0,
            trailing?.height ?: 0,
            center?.height ?: 0,
        )
        val layoutWidth = screenWidth
        val layoutHeight = contentHeight.coerceIn(constraints.minHeight, constraints.maxHeight)
        layout(layoutWidth, layoutHeight) {
            leading?.placeRelative(x = 0, y = (layoutHeight - leading.height) / 2)
            trailing?.placeRelative(
                x = layoutWidth - trailing.width,
                y = (layoutHeight - trailing.height) / 2,
            )
            center?.placeRelative(
                x = (layoutWidth - center.width) / 2,
                y = (layoutHeight - center.height) / 2,
            )
        }
    }
}

@Composable
private fun PommeBottomToolbar(
    items: List<PommeToolbarItemDeclaration>,
    onHeightChanged: (Int) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .onSizeChanged { onHeightChanged(it.height) },
    ) {
        if (items.isNotEmpty()) {
            PommeToolbarItemsRow(
                items = items,
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = maxWidth)
                    .clipToBounds()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun PommeToolbarItemsRow(
    items: List<PommeToolbarItemDeclaration>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            CompositionLocalProvider(LocalPommeToolbarContext provides true) {
                item.content()
            }
        }
    }
}

@Composable
private fun PommeToolbarBackground(
    isTop: Boolean,
    contentHeight: Dp,
    insets: WindowInsets,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = PommeTheme.colorScheme.background
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                val gradientColors = if (isTop) {
                    listOf(
                        backgroundColor.copy(alpha = 0.92f),
                        backgroundColor.copy(alpha = 0.72f),
                        Color.Transparent,
                    )
                } else {
                    listOf(
                        Color.Transparent,
                        backgroundColor.copy(alpha = 0.72f),
                        backgroundColor.copy(alpha = 0.92f),
                    )
                }
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = gradientColors,
                        startY = 0f,
                        endY = size.height,
                    ),
                )
            }
            .windowInsetsPadding(insets)
            .height(contentHeight + 24.dp),
    )
}

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
internal annotation class PommeScaffoldLightDarkPreview

@PommeScaffoldLightDarkPreview
@Composable
internal fun PommeScaffoldPreview() {
    PommeScaffoldPreviewTheme {
        PommeScaffold {
            navigationTitle("基本情報")
            content {
                PommeScaffoldPreviewContent("コンテンツは Toolbar の下に表示されます")
            }
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {
                    Text("戻る", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {
                    Text("編集", color = PommeTheme.colorScheme.accentColor)
                }
                PommeToolbarItem(PommeToolbarPlacement.BottomBar) {
                    Text("地図", color = PommeTheme.colorScheme.accentColor)
                }
            }
        }
    }
}

@PommeScaffoldLightDarkPreview
@Composable
internal fun PommeScaffoldSafeAreaPreview() {
    PommeScaffoldPreviewTheme {
        PommeScaffold {
            navigationTitle("安全領域を確保")
            safeArea(PommeSafeAreaEdge.Top, PommeSafeAreaEdge.Bottom)
            toolbarBackgroundVisibility(top = true, bottom = false)
            content {
                PommeScaffoldPreviewContent("上部・下部 Toolbar の内側にコンテンツを確保")
            }
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

@PommeScaffoldLightDarkPreview
@Composable
internal fun PommeScaffoldNoToolbarPreview() {
    PommeScaffoldPreviewTheme {
        PommeScaffold {
            safeArea(PommeSafeAreaEdge.Top, PommeSafeAreaEdge.Bottom)
            content {
                PommeScaffoldPreviewContent("Toolbar がない場合はシステム安全領域まで")
            }
        }
    }
}

@PommeScaffoldLightDarkPreview
@Composable
internal fun PommeScaffoldToolbarBackgroundVisibilityPreview() {
    PommeScaffoldPreviewTheme {
        PommeScaffold {
            toolbarBackgroundVisibility(top = false, bottom = true)
            content {
                PommeScaffoldPreviewContent("下部だけグラデーションを表示")
            }
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

@PommeScaffoldLightDarkPreview
@Composable
internal fun PommeScaffoldPrincipalPreview() {
    PommeScaffoldPreviewTheme {
        PommeScaffold {
            navigationTitle("Navigation title")
            content { PommeScaffoldPreviewContent("Principal item が navigationTitle より優先") }
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

@PommeScaffoldLightDarkPreview
@Composable
internal fun PommeScaffoldLongTitlePreview() {
    PommeScaffoldPreviewTheme {
        PommeScaffold {
            navigationTitle("複数の Toolbar Item と競合する長いナビゲーションタイトル")
            content { PommeScaffoldPreviewContent("長いタイトルは一行に省略") }
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

@PommeScaffoldLightDarkPreview
@Composable
internal fun PommeScaffoldRtlPreview() {
    PommeScaffoldPreviewTheme {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Rtl,
        ) {
            PommeScaffold {
                navigationTitle("RTL レイアウト")
                content { PommeScaffoldPreviewContent("Leading / Trailing は論理方向に配置") }
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
private fun PommeScaffoldPreviewTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme {
        PommeTheme(
            colorScheme = if (darkTheme) PommeColorScheme.dark() else PommeColorScheme.light(),
            content = content,
        )
    }
}

@Composable
private fun PommeScaffoldPreviewContent(text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PommeTheme.colorScheme.groupedBackground)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "コンテンツ先頭", color = PommeTheme.colorScheme.secondary)
            Text(text = text, color = PommeTheme.colorScheme.primary)
        }
        Text(text = "コンテンツ末尾", color = PommeTheme.colorScheme.secondary)
    }
}
