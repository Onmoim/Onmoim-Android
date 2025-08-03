package com.onmoim.core.data.model

import java.time.LocalDateTime

data class Message(
    val messageSequence: Int,
    val groupId: Int,
    val senderId: Int,
    val userName: String,
    val profileImageUrl: String?,
    val content: String,
    val sendDateTime: LocalDateTime,
    val isOwner: Boolean
)
