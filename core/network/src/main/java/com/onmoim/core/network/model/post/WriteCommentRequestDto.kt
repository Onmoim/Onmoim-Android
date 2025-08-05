package com.onmoim.core.network.model.post


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WriteCommentRequestDto(
    @SerialName("content")
    val content: String
)