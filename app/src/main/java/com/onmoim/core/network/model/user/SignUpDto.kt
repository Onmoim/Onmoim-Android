package com.onmoim.core.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("status")
    val status: String,
    @SerialName("userId")
    val userId: Int
)