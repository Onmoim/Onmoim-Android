package com.onmoim.core.network.model.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDetailDto(
    @SerialName("meetingId")
    val meetingId: Int,
    @SerialName("attendance")
    val attendance: Boolean,
    @SerialName("capacity")
    val capacity: Int,
    @SerialName("cost")
    val cost: Int,
    @SerialName("imgUrl")
    val imgUrl: String?,
    @SerialName("joinCount")
    val joinCount: Int,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String
)