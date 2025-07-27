package com.onmoim.feature.groups.state

sealed class PostDetailEvent {
    data class PostLikeFailure(val t: Throwable) : PostDetailEvent()
}