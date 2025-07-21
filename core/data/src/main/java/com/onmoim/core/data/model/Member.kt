package com.onmoim.core.data.model

import com.onmoim.core.data.constant.GroupMemberRole

data class Member(
    val id: Int,
    val name: String,
    val profileImageUrl: String?,
    val role: GroupMemberRole
)