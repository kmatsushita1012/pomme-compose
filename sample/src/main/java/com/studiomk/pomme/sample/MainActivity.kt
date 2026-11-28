package com.studiomk.pomme.sample

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.studiomk.pomme.PommeColorScheme

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "PommeColorScheme",
                color = colorScheme.primary,
                style = MaterialTheme.typography.headlineSmall,
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

@Preview(
    name = "Sample Light",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_NO
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
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
internal fun PommeSampleDarkPreview() {
    PommeSampleContent(darkTheme = true)
}
