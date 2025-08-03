package com.onmoim.core.data.constant

sealed class SocketConnectionState {
    data object Connected : SocketConnectionState()
    data object Connecting : SocketConnectionState()
    data object Disconnected : SocketConnectionState()
    data object Disconnecting : SocketConnectionState()
    data object NotAuthenticated : SocketConnectionState()
    data class Error(val t: Throwable) : SocketConnectionState()
}