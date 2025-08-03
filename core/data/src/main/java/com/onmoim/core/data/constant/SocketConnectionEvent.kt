package com.onmoim.core.data.constant

sealed class SocketConnectionEvent {
    data object Connected : SocketConnectionEvent()
    data object Connecting : SocketConnectionEvent()
    data object Disconnected : SocketConnectionEvent()
    data object Disconnecting : SocketConnectionEvent()
    data object NotAuthenticated : SocketConnectionEvent()
    data class Error(val t: Throwable) : SocketConnectionEvent()
}