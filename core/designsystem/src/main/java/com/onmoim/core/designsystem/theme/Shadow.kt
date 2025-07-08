package com.onmoim.core.designsystem.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.onmoim.core.ui.advancedShadow

fun Modifier.shadow1(
    cornersRadius: Dp = 0.dp
) = this.advancedShadow(
    color = Color(0xFF878787),
    alpha = 0.25f,
    cornersRadius = cornersRadius,
    shadowBlurRadius = 3.dp,
    offsetX = 0.dp,
    offsetY = 1.dp
)

fun Modifier.shadow2Block(
    cornersRadius: Dp = 0.dp
) = this.advancedShadow(
    color = Color(0xFF878787),
    alpha = 0.2f,
    cornersRadius = cornersRadius,
    shadowBlurRadius = 10.dp,
    offsetX = 0.dp,
    offsetY = 3.dp
)