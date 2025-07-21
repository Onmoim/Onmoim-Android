package com.onmoim.feature.groups.state

sealed class GroupDetailEvent {
    data object LeaveGroupSuccess : GroupDetailEvent()
    data class LeaveGroupFailure(val t: Throwable) : GroupDetailEvent()
}