package com.onmoim.feature.groups.state

import com.onmoim.feature.groups.constant.BoardType

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
    data object JoinGroupSuccess : GroupDetailEvent()
    data object JoinGroupBanned : GroupDetailEvent()
    data object JoinGroupNotFound : GroupDetailEvent()
    data object JoinGroupOverCapacity : GroupDetailEvent()
    data class JoinGroupFailure(val t: Throwable) : GroupDetailEvent()
    data class RefreshBoard(val type: BoardType) : GroupDetailEvent()
}