package com.studiomk.pomme.sample

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studiomk.pomme.PommeColorScheme
import com.studiomk.pomme.PommeTypography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PommeSampleContent(darkTheme = isSystemInDarkTheme())
        }
    }
}

@Composable
internal fun PommeSampleContent(darkTheme: Boolean) {
    val colorScheme = if (darkTheme) PommeColorScheme.dark() else PommeColorScheme.light()

    MaterialTheme(colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorScheme.background,
            contentColor = colorScheme.primary,
        ) {
            val typography = PommeTypography()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .safeDrawingPadding()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "PommeTypography", style = typography.headline)
                PommeTypographySampleLine("largeTitle", typography.largeTitle, typography.caption2)
                PommeTypographySampleLine("title", typography.title, typography.caption2)
                PommeTypographySampleLine("title2", typography.title2, typography.caption2)
                PommeTypographySampleLine("title3", typography.title3, typography.caption2)
                PommeTypographySampleLine("headline", typography.headline, typography.caption2)
                PommeTypographySampleLine("body", typography.body, typography.caption2)
                PommeTypographySampleLine("callout", typography.callout, typography.caption2)
                PommeTypographySampleLine("subheadline", typography.subheadline, typography.caption2)
                PommeTypographySampleLine("footnote", typography.footnote, typography.caption2)
                PommeTypographySampleLine("caption", typography.caption, typography.caption2)
                PommeTypographySampleLine("caption2", typography.caption2, typography.caption2)

                Text(
                    text = "PommeColorScheme",
                    color = colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 16.dp),
                )
                Text(text = "Primary text", color = colorScheme.primary)
                Text(text = "Secondary text", color = colorScheme.secondary)

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = colorScheme.groupedBackground,
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(text = "Grouped background", color = colorScheme.primary)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = colorScheme.secondaryGroupedBackground,
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = "Secondary grouped background",
                                    color = colorScheme.primary,
                                )
                                Text(text = "Accent color", color = colorScheme.accentColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PommeTypographySampleLine(label: String, style: TextStyle, labelStyle: TextStyle) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(text = label, modifier = Modifier.width(84.dp), style = labelStyle)
        Text(text = "Sample text", style = style)
    }
}

@Preview(
    name = "Sample Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Composable
internal fun PommeSampleLightPreview() {
    PommeSampleContent(darkTheme = false)
}

@Preview(
    name = "Sample Dark",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
internal fun PommeSampleDarkPreview() {
    PommeSampleContent(darkTheme = true)
}
