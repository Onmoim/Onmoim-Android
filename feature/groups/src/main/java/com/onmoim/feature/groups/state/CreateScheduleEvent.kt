package com.onmoim.feature.groups.state

sealed class CreateScheduleEvent {
    data object CreateSuccess: CreateScheduleEvent()
    data class CreateFailure(val t: Throwable): CreateScheduleEvent()
}