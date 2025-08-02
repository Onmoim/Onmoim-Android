package com.onmoim.core.network.model.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatSendTextDto(
    @SerialName("content")
    val content: String,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("type")
    val type: String
)