package com.onmoim.core.network.model.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SystemTopicDto(
    @SerialName("content")
    val content: String,
    @SerialName("timestamp")
    val timestamp: List<Int>,
    @SerialName("type")
    val type: String
)