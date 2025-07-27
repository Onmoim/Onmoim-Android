package com.onmoim.core.data.model

import com.onmoim.core.data.constant.MemberStatus

data class HomeGroup(
    val id: Int,
    val imageUrl: String?,
    val title: String,
    val location: String,
    val memberCount: Int,
    val scheduleCount: Int,
    val categoryName: String,
    val memberStatus: MemberStatus,
    val isFavorite: Boolean
)
