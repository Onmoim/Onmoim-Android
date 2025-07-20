package com.onmoim.feature.groups.state

sealed class GroupOpenEvent {
    data class CreateGroupSuccess(val groupId: Int) : GroupOpenEvent()
    data class CreateGroupError(val t: Throwable) : GroupOpenEvent()
}