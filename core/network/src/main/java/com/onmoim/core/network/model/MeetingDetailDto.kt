package com.onmoim.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDetailDto(
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
    val latitude: Int,
    @SerialName("longitude")
    val longitude: Int,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("startDate")
    val startDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String
)