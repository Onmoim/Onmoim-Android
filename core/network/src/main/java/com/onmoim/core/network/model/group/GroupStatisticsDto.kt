package com.onmoim.core.network.model.group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupStatisticsDto(
    @SerialName("annualSchedule")
    val annualSchedule: Int,
    @SerialName("monthlySchedule")
    val monthlySchedule: Int
)