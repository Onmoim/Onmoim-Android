package com.onmoim.feature.groups.state

sealed class ReplyEvent {
    data class WriteReplyFailure(val t: Throwable) : ReplyEvent()
    data object WriteReplySuccess : ReplyEvent()
}