package com.onmoim.feature.groups.state

sealed class GroupDetailEvent {
    data object LeaveGroupSuccess : GroupDetailEvent()
    data class LeaveGroupFailure(val t: Throwable) : GroupDetailEvent()
    data object DeleteGroupSuccess : GroupDetailEvent()
    data class DeleteGroupFailure(val t: Throwable) : GroupDetailEvent()
    data class FavoriteGroupFailure(val t: Throwable) : GroupDetailEvent()
}