package com.onmoim.core.network.model.meeting


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    @SerialName("capacity")
    val capacity: Int,
    @SerialName("cost")
    val cost: Int,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("creatorId")
    val creatorId: Int,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("imgUrl")
    val imgUrl: String,
    @SerialName("joinCount")
    val joinCount: Int,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
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
)