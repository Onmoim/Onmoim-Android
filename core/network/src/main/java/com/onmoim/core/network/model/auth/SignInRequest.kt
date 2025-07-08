package com.onmoim.core.network.model.auth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("provider")
    val provider: String,
    @SerialName("token")
    val token: String
)