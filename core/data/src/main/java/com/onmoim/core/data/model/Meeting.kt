package com.onmoim.core.data.model

import com.onmoim.core.data.constant.MeetingType
import java.time.LocalDateTime

data class Meeting(
    val id: Int,
    val groupId: Int,
    val title: String,
    val placeName: String,
    val startDate: LocalDateTime,
    val cost: Int,
    val joinCount: Int,
    val capacity: Int,
    val type: MeetingType,
    val imgUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val attendance: Boolean,
)