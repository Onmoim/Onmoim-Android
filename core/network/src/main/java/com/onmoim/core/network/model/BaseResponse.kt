package com.onmoim.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("data")
    val data: T?,
    @SerialName("message")
    val message: String,
    @SerialName("timestamp")
    val timestamp: String
)