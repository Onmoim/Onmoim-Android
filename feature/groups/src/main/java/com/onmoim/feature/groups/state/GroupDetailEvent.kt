package com.onmoim.feature.groups.state

sealed class GroupDetailEvent {
    data object LeaveGroupSuccess : GroupDetailEvent()
    data class LeaveGroupFailure(val t: Throwable) : GroupDetailEvent()
    data object DeleteGroupSuccess : GroupDetailEvent()
    data class DeleteGroupFailure(val t: Throwable) : GroupDetailEvent()
    data class FavoriteGroupFailure(val t: Throwable) : GroupDetailEvent()
    data object MeetingNotFound : GroupDetailEvent()
    data object AttendMeetingSuccess : GroupDetailEvent()
    data object AttendMeetingOverCapacity : GroupDetailEvent()
    data class AttendMeetingFailure(val t: Throwable) : GroupDetailEvent()
    data object LeaveMeetingSuccess : GroupDetailEvent()
    data class LeaveMeetingFailure(val t: Throwable) : GroupDetailEvent()
}