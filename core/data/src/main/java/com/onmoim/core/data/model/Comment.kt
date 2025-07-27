package com.onmoim.core.data.model

import java.time.LocalDateTime

data class Comment(
    val id: Int,
    val authorId: Int,
    val userName: String,
    val profileImageUrl: String?,
    val content: String,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val replyCount: Int
)
