package com.onmoim.feature.groups.state

sealed class ComingScheduleEvent {
    data class FilterChangeAndLoadDateFailure(val t: Throwable) : ComingScheduleEvent()
    data object MeetingNotFound : ComingScheduleEvent()
    data object AttendMeetingSuccess : ComingScheduleEvent()
    data object AttendMeetingOverCapacity : ComingScheduleEvent()
    data class AttendMeetingFailure(val t: Throwable) : ComingScheduleEvent()
    data object LeaveMeetingSuccess : ComingScheduleEvent()
    data class LeaveMeetingFailure(val t: Throwable) : ComingScheduleEvent()
}