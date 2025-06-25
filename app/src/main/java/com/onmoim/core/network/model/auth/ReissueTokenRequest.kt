package com.onmoim.core.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReissueTokenRequest(
    @SerialName("refreshToken")
    val refreshToken: String
)