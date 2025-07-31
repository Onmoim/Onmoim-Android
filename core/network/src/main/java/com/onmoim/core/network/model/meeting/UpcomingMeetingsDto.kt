package com.onmoim.core.network.model.meeting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpcomingMeetingsDto(
    @SerialName("content")
    val content: List<Content>,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursorId")
    val nextCursorId: Int?,
    @SerialName("nextCursorStartAt")
    val nextCursorStartAt: String?
) {
    @Serializable
    data class Content(
        @SerialName("attendance")
        val attendance: Boolean,
        @SerialName("capacity")
        val capacity: Int,
        @SerialName("cost")
        val cost: Int,
        @SerialName("groupId")
        val groupId: Int,
        @SerialName("id")
        val id: Int,
        @SerialName("imgUrl")
        val imgUrl: String?,
        @SerialName("joinCount")
        val joinCount: Int,
        @SerialName("location")
        val location: Location,
        @SerialName("placeName")
        val placeName: String,
        @SerialName("startAt")
        val startAt: String,
        @SerialName("status")
        val status: String,
        @SerialName("title")
        val title: String,
        @SerialName("type")
        val type: String
    ) {
        @Serializable
        data class Location(
            @SerialName("x")
            val x: Double,
            @SerialName("y")
            val y: Double
        )
    }
}