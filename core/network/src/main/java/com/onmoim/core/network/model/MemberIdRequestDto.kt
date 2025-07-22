package com.onmoim.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberIdRequestDto(
    @SerialName("memberId")
    val memberId: Int?
)