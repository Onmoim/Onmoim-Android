package com.onmoim.core.network.model.chat


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomDto(
    @SerialName("category")
    val category: String,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("imgUrl")
    val imgUrl: String?,
    @SerialName("likeStatus")
    val likeStatus: String,
    @SerialName("location")
    val location: String,
    @SerialName("memberCount")
    val memberCount: Int,
    @SerialName("message")
    val message: Message,
    @SerialName("name")
    val name: String,
    @SerialName("recommendStatus")
    val recommendStatus: String?,
    @SerialName("status")
    val status: String,
    @SerialName("upcomingMeetingCount")
    val upcomingMeetingCount: Int
) {
    @Serializable
    data class Message(
        @SerialName("chatUserDto")
        val chatUserDto: ChatUserDto,
        @SerialName("content")
        val content: String,
        @SerialName("groupId")
        val groupId: Int,
        @SerialName("messageSequence")
        val messageSequence: Int,
        @SerialName("senderId")
        val senderId: Int?,
        @SerialName("timestamp")
        val timestamp: String,
        @SerialName("type")
        val type: String
    ) {
        @Serializable
        data class ChatUserDto(
            @SerialName("id")
            val id: Int?,
            @SerialName("owner")
            val owner: Boolean,
            @SerialName("profileImageUrl")
            val profileImageUrl: String?,
            @SerialName("username")
            val username: String?
        )
    }
}