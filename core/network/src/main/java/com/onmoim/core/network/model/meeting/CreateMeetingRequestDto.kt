package com.onmoim.core.network.model.meeting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateMeetingRequestDto(
    @SerialName("capacity")
    val capacity: Int,
    @SerialName("cost")
    val cost: Long,
    @SerialName("geoPoint")
    val geoPoint: GeoPoint,
    @SerialName("placeName")
    val placeName: String,
    @SerialName("startAt")
    val startAt: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String
) {
    @Serializable
    data class GeoPoint(
        @SerialName("x")
        val x: Double,
        @SerialName("y")
        val y: Double
    )
}