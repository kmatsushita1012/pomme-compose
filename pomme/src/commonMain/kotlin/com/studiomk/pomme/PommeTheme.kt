package com.studiomk.pomme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val LocalPommeColorScheme = staticCompositionLocalOf { PommeColorScheme.light() }
private val LocalPommeTypography = staticCompositionLocalOf { PommeTypography() }

/**
 * Provides Pomme colors and typography to Compose content.
 *
 * By default, the color scheme follows the system light or dark appearance and typography uses
 * [PommeTypography]'s standard values. Standard text components inherit the Pomme primary text
 * color and body style, while an explicit style or font weight takes precedence.
 *
 * @param colorScheme The color scheme to provide. Defaults to the standard palette for the system
 *   appearance.
 * @param typography The typography to provide. Defaults to [PommeTypography].
 * @param content The content that receives this theme.
 */
@Composable
public fun PommeTheme(
    colorScheme: PommeColorScheme =
        if (isSystemInDarkTheme()) PommeColorScheme.dark() else PommeColorScheme.light(),
    typography: PommeTypography = PommeTypography(),
    content: @Composable () -> Unit,
) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalPommeColorScheme provides colorScheme,
        LocalPommeTypography provides typography,
        LocalContentColor provides colorScheme.primary,
        LocalTextStyle provides typography.body,
    ) {
        content()
    }
}

/** Values currently provided by [PommeTheme]. */
public object PommeTheme {
    /** The color scheme currently provided by [PommeTheme]. */
    public val colorScheme: PommeColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalPommeColorScheme.current

    /** The typography currently provided by [PommeTheme]. */
    public val typography: PommeTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalPommeTypography.current
}

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 240,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 240,
)
internal annotation class PommeThemeLightDarkPreview

@PommeThemeLightDarkPreview
@Composable
internal fun PommeThemePreview() {
    MaterialTheme {
        PommeTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PommeTheme.colorScheme.background)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(text = "PommeTheme", style = PommeTheme.typography.headline)
                Text(text = "Default body text")
                Text(text = "Explicit title", style = PommeTheme.typography.title)
                Text(text = "Explicit bold weight", fontWeight = FontWeight.Bold)
            }
        }
    }
}
