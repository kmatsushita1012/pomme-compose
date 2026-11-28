package com.studiomk.pomme.sample

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.studiomk.pomme.PommeLazyRowsPreview
import com.studiomk.pomme.PommeRowsLayoutPreview

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 560,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 560,
)
internal annotation class PommeRowsLayoutScreenshotPreviews

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    widthDp = 360,
    heightDp = 340,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    widthDp = 360,
    heightDp = 340,
)
internal annotation class PommeLazyRowsScreenshotPreviews

@PreviewTest
@PommeRowsLayoutScreenshotPreviews
@Composable
fun PommeRowsLayoutScreenshotTest() {
    PommeRowsLayoutPreview()
}

@PreviewTest
@PommeLazyRowsScreenshotPreviews
@Composable
fun PommeLazyRowsScreenshotTest() {
    PommeLazyRowsPreview()
}
