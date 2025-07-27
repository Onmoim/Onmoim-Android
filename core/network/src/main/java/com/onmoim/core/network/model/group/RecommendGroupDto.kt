package com.onmoim.core.network.model.group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendGroupDto(
    @SerialName("content")
    val content: List<Content>,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursorId")
    val nextCursorId: Int?
) {
    @Serializable
    data class Content(
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
        @SerialName("name")
        val name: String,
        @SerialName("recommendStatus")
        val recommendStatus: String,
        @SerialName("status")
        val status: String,
        @SerialName("upcomingMeetingCount")
        val upcomingMeetingCount: Int
    )
}