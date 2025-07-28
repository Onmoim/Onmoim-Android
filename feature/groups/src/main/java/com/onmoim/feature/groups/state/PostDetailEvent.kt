package com.onmoim.feature.groups.state

sealed class PostDetailEvent {
    data class PostLikeFailure(val t: Throwable) : PostDetailEvent()
    data class CommentWriteFailure(val t: Throwable) : PostDetailEvent()
    data object CommentWriteSuccess : PostDetailEvent()
}