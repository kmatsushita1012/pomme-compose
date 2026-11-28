package com.studiomk.pomme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PommeScaffoldTest {
    @Test
    fun toolbarItemsKeepAllPlacementsAndDeclarationOrder() {
        val declaration = PommeScaffoldScopeState().apply {
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {}
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {}
                PommeToolbarItem(PommeToolbarPlacement.TopBarTrailing) {}
                PommeToolbarItem(PommeToolbarPlacement.Principal) {}
                PommeToolbarItem(PommeToolbarPlacement.BottomBar) {}
            }
        }.toDeclaration()

        assertEquals(
            listOf(
                PommeToolbarPlacement.TopBarTrailing,
                PommeToolbarPlacement.TopBarLeading,
                PommeToolbarPlacement.TopBarTrailing,
                PommeToolbarPlacement.Principal,
                PommeToolbarPlacement.BottomBar,
            ),
            declaration.toolbarItems.map { it.placement },
        )
        assertTrue(declaration.hasPrincipalItem)
        assertTrue(declaration.hasTopToolbarContent)
        assertTrue(declaration.hasBottomToolbarContent)
    }

    @Test
    fun rebuildingTheScopeDropsConditionallyRemovedItems() {
        fun declaration(showOptionalItem: Boolean): PommeScaffoldDeclaration =
            PommeScaffoldScopeState().apply {
                toolbar {
                    PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {}
                    if (showOptionalItem) {
                        PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {}
                    }
                }
            }.toDeclaration()

        val withOptionalItem = declaration(showOptionalItem = true)
        val withoutOptionalItem = declaration(showOptionalItem = false)

        assertEquals(2, withOptionalItem.toolbarItems.size)
        assertEquals(1, withoutOptionalItem.toolbarItems.size)
        assertEquals(PommeToolbarPlacement.TopBarLeading, withoutOptionalItem.toolbarItems.single().placement)
    }

    @Test
    fun principalContentTakesPrecedenceOverNavigationTitle() {
        val declaration = PommeScaffoldScopeState().apply {
            navigationTitle("Page title")
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.Principal) {}
            }
        }.toDeclaration()

        assertEquals("Page title", declaration.navigationTitle)
        assertTrue(declaration.hasPrincipalItem)
        assertNull(declaration.navigationTitle.takeUnless { declaration.hasPrincipalItem })
    }

    @Test
    fun safeAreaReservesOnlyTheRequestedToolbarHeight() {
        assertEquals(48, reservedToolbarHeightPx(safeAreaRequested = true, toolbarHeightPx = 48))
        assertEquals(0, reservedToolbarHeightPx(safeAreaRequested = false, toolbarHeightPx = 48))
        assertEquals(0, reservedToolbarHeightPx(safeAreaRequested = true, toolbarHeightPx = -1))
    }

    @Test
    fun safeAreaAndToolbarBackgroundVisibilityAreFreshDeclarations() {
        val declaration = PommeScaffoldScopeState().apply {
            safeArea(PommeSafeAreaEdge.Top, PommeSafeAreaEdge.Bottom)
            toolbarBackgroundVisibility(top = false, bottom = true)
            toolbar {
                PommeToolbarItem(PommeToolbarPlacement.TopBarLeading) {}
                PommeToolbarItem(PommeToolbarPlacement.BottomBar) {}
            }
        }.toDeclaration()

        assertEquals(setOf(PommeSafeAreaEdge.Top, PommeSafeAreaEdge.Bottom), declaration.safeAreaEdges)
        assertFalse(
            shouldShowToolbarBackground(
                explicitVisibility = declaration.toolbarBackgroundVisibility?.top,
                hasItems = declaration.hasTopToolbarContent,
            ),
        )
        assertTrue(
            shouldShowToolbarBackground(
                explicitVisibility = declaration.toolbarBackgroundVisibility?.bottom,
                hasItems = declaration.hasBottomToolbarContent,
            ),
        )
    }

    @Test
    fun toolbarBackgroundDefaultsToThePresenceOfItemsAndHonorsExplicitOverrides() {
        assertTrue(shouldShowToolbarBackground(explicitVisibility = null, hasItems = true))
        assertFalse(shouldShowToolbarBackground(explicitVisibility = null, hasItems = false))
        assertTrue(shouldShowToolbarBackground(explicitVisibility = true, hasItems = false))
        assertFalse(shouldShowToolbarBackground(explicitVisibility = false, hasItems = true))
    }
}
