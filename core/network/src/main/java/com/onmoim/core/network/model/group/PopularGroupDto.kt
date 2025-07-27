package com.onmoim.core.network.model.group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularGroupDto(
    @SerialName("content")
    val content: List<Content>,
    @SerialName("extraInfo")
    val extraInfo: ExtraInfo
) {
    @Serializable
    data class Content(
        @SerialName("category")
        val category: String,
        @SerialName("dong")
        val dong: String,
        @SerialName("groupId")
        val groupId: Int,
        @SerialName("imageUrl")
        val imageUrl: String?,
        @SerialName("memberCount")
        val memberCount: Int,
        @SerialName("name")
        val name: String,
        @SerialName("status")
        val status: String,
        @SerialName("upcomingMeetingCount")
        val upcomingMeetingCount: Int,
        @SerialName("likeStatus")
        val likeStatus: String
    )

    @Serializable
    data class ExtraInfo(
        @SerialName("hasNext")
        val hasNext: Boolean,
        @SerialName("lastGroupId")
        val lastGroupId: Int?
    )
}