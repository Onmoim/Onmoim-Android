package com.onmoim.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtraInfoDto(
    @SerialName("groupMemberCount")
    val groupMemberCount: Int,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("lastMemberId")
    val lastMemberId: Int
)