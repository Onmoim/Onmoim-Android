package com.onmoim.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

internal val ColorPalette = OnmoimColors(
    backgroundColor = ColorWhite,
    c565656 = Color565656,
    c69999999 = Color69999999,
    c2781FC = Color2781FC,
    c21C3AB = Color21C3AB,
    cEF61DF = ColorEF61DF,
    cF8F8F8 = ColorF8F8F8,
    cCDCDCD = ColorCDCDCD,
    cF97D7D = ColorF97D7D,
    cFF0033 = ColorFF0033,
)

internal val LocalOnmoimColors = staticCompositionLocalOf<OnmoimColors> {
    error("No OnmoimColorPalette provided")
}

@Composable
fun OnmoimTheme(
    content: @Composable () -> Unit
) {
    val colorPalette = remember { ColorPalette }

    CompositionLocalProvider(
        LocalOnmoimColors provides colorPalette,
        LocalTypography provides Typography
    ) {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                background = ColorPalette.backgroundColor,
                surface = ColorPalette.backgroundColor
            ),
            content = content
        )
    }
}

object OnmoimTheme {
    val colors: OnmoimColors
        @Composable
        get() = LocalOnmoimColors.current
    val typography: OnmoimTypography
        @Composable
        get() = LocalTypography.current
}

@Stable
class OnmoimColors(
    backgroundColor: Color,
    c565656: Color,
    c69999999: Color,
    c2781FC: Color,
    c21C3AB: Color,
    cEF61DF: Color,
    cF8F8F8: Color,
    cCDCDCD: Color,
    cF97D7D: Color,
    cFF0033: Color,
) {
    var backgroundColor by mutableStateOf(backgroundColor)
        private set
    var c565656 by mutableStateOf(c565656)
        private set
    var c69999999 by mutableStateOf(c69999999)
        private set
    var c2781FC by mutableStateOf(c2781FC)
        private set
    var c21C3AB by mutableStateOf(c21C3AB)
        private set
    var cEF61DF by mutableStateOf(cEF61DF)
        private set
    var cF8F8F8 by mutableStateOf(cF8F8F8)
        private set
    var cCDCDCD by mutableStateOf(cCDCDCD)
        private set
    var cF97D7D by mutableStateOf(cF97D7D)
        private set
    var cFF0033 by mutableStateOf(cFF0033)
        private set

    fun update(other: OnmoimColors) {
        backgroundColor = other.backgroundColor
        c565656 = other.c565656
        c69999999 = other.c69999999
        c2781FC = other.c2781FC
        c21C3AB = other.c21C3AB
        cEF61DF = other.cEF61DF
        cF8F8F8 = other.cF8F8F8
        cCDCDCD = other.cCDCDCD
        cF97D7D = other.cF97D7D
        cFF0033 = other.cFF0033
    }

    fun copy(): OnmoimColors = OnmoimColors(
        backgroundColor = backgroundColor,
        c565656 = c565656,
        c69999999 = c69999999,
        c2781FC = c2781FC,
        c21C3AB = c21C3AB,
        cEF61DF = cEF61DF,
        cF8F8F8 = cF8F8F8,
        cCDCDCD = cCDCDCD,
        cF97D7D = cF97D7D,
        cFF0033 = cFF0033,
    )
}