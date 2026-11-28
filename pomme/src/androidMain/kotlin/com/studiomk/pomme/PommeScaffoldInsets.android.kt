package com.studiomk.pomme

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable

@Composable
internal actual fun PommeScaffoldSafeDrawingInsets(): WindowInsets = WindowInsets.safeDrawing
