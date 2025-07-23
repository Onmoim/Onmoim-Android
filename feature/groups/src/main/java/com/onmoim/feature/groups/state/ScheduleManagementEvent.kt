package com.onmoim.feature.groups.state

sealed class ScheduleManagementEvent {
    data object DeleteSuccess: ScheduleManagementEvent()
    data class DeleteFailure(val t: Throwable): ScheduleManagementEvent()
}