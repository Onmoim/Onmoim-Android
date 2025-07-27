package com.onmoim.core.network.model.post


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequestDto(
    @SerialName("content")
    val content: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String
)