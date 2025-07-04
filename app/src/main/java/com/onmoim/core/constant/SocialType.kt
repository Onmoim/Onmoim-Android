package com.onmoim.core.constant

import androidx.compose.ui.graphics.Color
import com.onmoim.R

enum class SocialType(
    val labelId: Int,
    val color: Color
) {
    GOOGLE(R.string.btn_google_login, Color.White),
    KAKAO(R.string.btn_kakao_login, Color(0xFFFFE400)),
}