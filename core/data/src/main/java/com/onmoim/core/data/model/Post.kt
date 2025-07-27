package com.onmoim.core.data.model

import com.onmoim.core.data.constant.PostType
import java.time.LocalDateTime

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    val name: String,
    val profileImageUrl: String?,
    val type: PostType,
    val createdDate: LocalDateTime,
    val modifiedDate: LocalDateTime,
    val imageUrls: List<String>,
    val likeCount: Int,
    val isLiked: Boolean,
    val commentCount: Int
)
