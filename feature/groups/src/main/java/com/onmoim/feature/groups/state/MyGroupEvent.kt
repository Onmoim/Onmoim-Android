package com.onmoim.feature.groups.state

sealed class MyGroupEvent {
    data object MeetingNotFound : MyGroupEvent()
    data object AttendMeetingSuccess : MyGroupEvent()
    data object AttendMeetingOverCapacity : MyGroupEvent()
    data class AttendMeetingFailure(val t: Throwable) : MyGroupEvent()
    data object LeaveMeetingSuccess : MyGroupEvent()
    data class LeaveMeetingFailure(val t: Throwable) : MyGroupEvent()
}