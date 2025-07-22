package com.onmoim.feature.groups.state

sealed class GroupEditEvent {
    data object EditSuccess: GroupEditEvent()
    data class EditFailure(val t: Throwable): GroupEditEvent()
}