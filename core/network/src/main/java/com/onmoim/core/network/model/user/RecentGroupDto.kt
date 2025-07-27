package com.onmoim.core.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentGroupDto(
    @SerialName("content")
    val content: List<Content>,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursorLogId")
    val nextCursorLogId: Int?,
    @SerialName("nextCursorViewedAt")
    val nextCursorViewedAt: String?
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
        val upcomingMeetingCount: Int,
        @SerialName("viewedAt")
        val viewedAt: String
    )
}