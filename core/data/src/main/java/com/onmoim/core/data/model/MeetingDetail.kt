package com.onmoim.core.data.model

import java.time.LocalDateTime

data class MeetingDetail(
    val id: Int,
    val title: String,
    val placeName: String,
    val startDate: LocalDateTime,
    val cost: Int,
    val joinCount: Int,
    val capacity: Int,
    val attendance: Boolean,
    val isLightning: Boolean,
    val imgUrl: String?,
    val latitude: Int,
    val longitude: Int
)
