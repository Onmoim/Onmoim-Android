package com.onmoim.core.network.socket

sealed class GroupChatConnectionEvent {
    data object Connected: GroupChatConnectionEvent()
    data object Connecting: GroupChatConnectionEvent()
    data object Disconnected: GroupChatConnectionEvent()
    data object Disconnecting: GroupChatConnectionEvent()
    data object NotAuthenticated: GroupChatConnectionEvent()
    data class Error(val t: Throwable): GroupChatConnectionEvent()
}