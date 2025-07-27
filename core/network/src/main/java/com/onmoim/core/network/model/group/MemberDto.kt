package com.onmoim.core.network.model.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberDto(
    @SerialName("memberId")
    val memberId: Int,
    @SerialName("profileImageUrl")
    val profileImageUrl: String?,
    @SerialName("role")
    val role: String,
    @SerialName("username")
    val username: String
)