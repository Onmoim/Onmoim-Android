package com.onmoim.feature.groups.state

sealed class PostWriteEvent {
    data object CreatePostSuccess: PostWriteEvent()
    data class CreatePostFailure(val t: Throwable): PostWriteEvent()
}