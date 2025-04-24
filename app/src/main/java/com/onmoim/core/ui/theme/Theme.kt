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
    backgroundColor = BgWhite,
    textColor = TextBlack,
    primaryBlue = PrimaryBlue,
    primaryMint = PrimaryMint,
    primaryPink = PrimaryPink,
    gray01 = Gray01,
    gray02 = Gray02,
    gray03 = Gray03,
    gray04 = Gray04,
    gray05 = Gray05,
    gray06 = Gray06,
    accentSoftRed = AccentSoftRed,
    alertRed = AlertRed,
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
    textColor: Color,
    primaryBlue: Color,
    primaryMint: Color,
    primaryPink: Color,
    gray01: Color,
    gray02: Color,
    gray03: Color,
    gray04: Color,
    gray05: Color,
    gray06: Color,
    accentSoftRed: Color,
    alertRed: Color,
) {
    var backgroundColor by mutableStateOf(backgroundColor)
        private set
    var textColor by mutableStateOf(textColor)
        private set
    var primaryBlue by mutableStateOf(primaryBlue)
        private set
    var primaryMint by mutableStateOf(primaryMint)
        private set
    var primaryPink by mutableStateOf(primaryPink)
        private set
    var gray01 by mutableStateOf(gray01)
        private set
    var gray02 by mutableStateOf(gray02)
        private set
    var gray03 by mutableStateOf(gray03)
        private set
    var gray04 by mutableStateOf(gray04)
        private set
    var gray05 by mutableStateOf(gray05)
        private set
    var gray06 by mutableStateOf(gray06)
        private set
    var accentSoftRed by mutableStateOf(accentSoftRed)
        private set
    var alertRed by mutableStateOf(alertRed)
        private set

    fun update(other: OnmoimColors) {
        backgroundColor = other.backgroundColor
        textColor = other.textColor
        primaryBlue = other.primaryBlue
        primaryMint = other.primaryMint
        primaryPink = other.primaryPink
        gray01 = other.gray01
        gray02 = other.gray02
        gray03 = other.gray03
        gray04 = other.gray04
        gray05 = other.gray05
        gray06 = other.gray06
        accentSoftRed = other.accentSoftRed
        alertRed = other.alertRed
    }

    fun copy(): OnmoimColors = OnmoimColors(
        backgroundColor = backgroundColor,
        textColor = textColor,
        primaryBlue = primaryBlue,
        primaryMint = primaryMint,
        primaryPink = primaryPink,
        gray01 = gray01,
        gray02 = gray02,
        gray03 = gray03,
        gray04 = gray04,
        gray05 = gray05,
        gray06 = gray06,
        accentSoftRed = accentSoftRed,
        alertRed = alertRed,
    )
}