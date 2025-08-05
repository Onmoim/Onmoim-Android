package com.onmoim.feature.groups.state

sealed class PostDetailEvent {
    data class PostLikeFailure(val t: Throwable) : PostDetailEvent()
    data class CommentWriteFailure(val t: Throwable) : PostDetailEvent()
    data object CommentWriteSuccess : PostDetailEvent()
    data object CommentUpdateSuccess: PostDetailEvent()
    data class CommentUpdateFailure(val t: Throwable) : PostDetailEvent()
    data object CommentDeleteSuccess: PostDetailEvent()
    data class CommentDeleteFailure(val t: Throwable) : PostDetailEvent()
}