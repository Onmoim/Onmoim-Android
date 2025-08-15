package com.onmoim.core.data.model

import java.time.LocalDateTime

data class ChatRoom(
    val groupId: Int,
    val title: String,
    val roomMemberCount: Int,
    val lastSentDateTime: LocalDateTime,
    val lastSentMessage: String,
    val imageUrl: String?
)