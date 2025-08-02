package com.onmoim.core.network.model.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageDto(
    @SerialName("content")
    val content: String,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("deleted")
    val deleted: Boolean,
    @SerialName("deletedDate")
    val deletedDate: String?,
    @SerialName("deliveryStatus")
    val deliveryStatus: String,
    @SerialName("id")
    val id: Id,
    @SerialName("modifiedDate")
    val modifiedDate: String,
    @SerialName("senderId")
    val senderId: Int,
    @SerialName("timestamp")
    val timestamp: String,
    @SerialName("type")
    val type: String
) {
    @Serializable
    data class Id(
        @SerialName("messageSequence")
        val messageSequence: Int,
        @SerialName("roomId")
        val roomId: Int
    )
}