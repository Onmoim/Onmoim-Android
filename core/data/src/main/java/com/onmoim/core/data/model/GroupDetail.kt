package com.onmoim.core.data.model

import com.onmoim.core.data.constant.MemberStatus

data class GroupDetail(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val location: String,
    val category: String,
    val categoryIconUrl: String,
    val memberCount: Int,
    val description: String,
    val meetingList: List<MeetingDetail>,
    val isFavorite: Boolean,
    val memberStatus: MemberStatus,
    val capacity: Int,
)
