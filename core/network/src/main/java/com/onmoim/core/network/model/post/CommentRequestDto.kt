package com.onmoim.core.network.model.post


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentRequestDto(
    @SerialName("content")
    val content: String
)