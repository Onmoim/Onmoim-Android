package com.onmoim.feature.groups.state

sealed class GroupManagementEvent {
    data object BanSuccess : GroupManagementEvent()
    data class BanFailure(val t: Throwable) : GroupManagementEvent()
    data object TransferOwnerSuccess : GroupManagementEvent()
    data class TransferOwnerFailure(val t: Throwable) : GroupManagementEvent()
}