package com.onmoim.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.onmoim.R

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val inter = FontFamily(
    androidx.compose.ui.text.googlefonts.Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = fontProvider,
        weight = FontWeight.W700
    )
)

val pretendard = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.W100),
    Font(R.font.pretendard_extra_light, FontWeight.W200),
    Font(R.font.pretendard_light, FontWeight.W300),
    Font(R.font.pretendard_regular, FontWeight.W400),
    Font(R.font.pretendard_medium, FontWeight.W500),
    Font(R.font.pretendard_semi_bold, FontWeight.W600),
    Font(R.font.pretendard_bold, FontWeight.W700),
    Font(R.font.pretendard_extra_bold, FontWeight.W800),
    Font(R.font.pretendard_black, FontWeight.W900),
)

private val pretendardStyle = TextStyle(
    fontFamily = pretendard
)

internal val Typography = OnmoimTypography(
    title1Bold = pretendardStyle.copy(
        fontSize = 32.sp,
        fontWeight = FontWeight.W700
    ),
    title2Bold = pretendardStyle.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.W700
    ),
    title3Bold = pretendardStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.W700
    ),
    body1Bold = pretendardStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.W700
    ),
    body1SemiBold = pretendardStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.W600
    ),
    body2Bold = pretendardStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.W700
    ),
    body2SemiBold = pretendardStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.W600
    ),
    body2Regular = pretendardStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.W400
    ),
    caption1Bold = pretendardStyle.copy(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.W700
    ),
    caption1SemiBold = pretendardStyle.copy(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.W600
    ),
    caption1Regular = pretendardStyle.copy(
        fontSize = 13.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.W400
    ),
    caption2Bold = pretendardStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.W700
    ),
    caption2SemiBold = pretendardStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.W600
    ),
    caption2Regular = pretendardStyle.copy(
        fontSize = 12.sp,
        fontWeight = FontWeight.W400
    ),
    caption3Regular = pretendardStyle.copy(
        fontSize = 11.sp,
        fontWeight = FontWeight.W400
    ),
)

@Immutable
data class OnmoimTypography(
    val title1Bold: TextStyle,
    val title2Bold: TextStyle,
    val title3Bold: TextStyle,
    val body1Bold: TextStyle,
    val body1SemiBold: TextStyle,
    val body2Bold: TextStyle,
    val body2SemiBold: TextStyle,
    val body2Regular: TextStyle,
    val caption1Bold: TextStyle,
    val caption1SemiBold: TextStyle,
    val caption1Regular: TextStyle,
    val caption2Bold: TextStyle,
    val caption2SemiBold: TextStyle,
    val caption2Regular: TextStyle,
    val caption3Regular: TextStyle,
)

val LocalTypography = staticCompositionLocalOf {
    OnmoimTypography(
        title1Bold = pretendardStyle,
        title2Bold = pretendardStyle,
        title3Bold = pretendardStyle,
        body1Bold = pretendardStyle,
        body1SemiBold = pretendardStyle,
        body2Bold = pretendardStyle,
        body2SemiBold = pretendardStyle,
        body2Regular = pretendardStyle,
        caption1Bold = pretendardStyle,
        caption1SemiBold = pretendardStyle,
        caption1Regular = pretendardStyle,
        caption2Bold = pretendardStyle,
        caption2SemiBold = pretendardStyle,
        caption2Regular = pretendardStyle,
        caption3Regular = pretendardStyle,
    )
}