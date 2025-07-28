package com.onmoim.core.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequestDto(
    @SerialName("birth")
    val birth: String,
    @SerialName("categoryIdList")
    val categoryIdList: List<Int>,
    @SerialName("gender")
    val gender: String,
    @SerialName("introduction")
    val introduction: String,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("name")
    val name: String,
    @SerialName("profileImgUrl")
    val profileImgUrl: String?
)