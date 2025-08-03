package com.onmoim.core.network.model.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomTopicDto(
    @SerialName("chatUserDto")
    val chatUserDto: ChatUserDto,
    @SerialName("content")
    val content: String,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("messageSequence")
    val messageSequence: Int,
    @SerialName("timestamp")
    val timestamp: List<Int>,
    @SerialName("type")
    val type: String,
    @SerialName("senderId")
    val senderId: Int
) {
    @Serializable
    data class ChatUserDto(
        @SerialName("id")
        val id: Int,
        @SerialName("owner")
        val owner: Boolean,
        @SerialName("profileImageUrl")
        val profileImageUrl: String?,
        @SerialName("username")
        val username: String
    )
}