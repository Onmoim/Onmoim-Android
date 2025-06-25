package com.onmoim.core.data.model

data class Jwt(
    val accessToken: String,
    val refreshToken: String?
)
