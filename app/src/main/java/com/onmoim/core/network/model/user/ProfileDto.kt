package com.onmoim.core.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("birth")
    val birth: String,
    @SerialName("categoryList")
    val categoryList: List<String>,
    @SerialName("id")
    val id: Int,
    @SerialName("introduction")
    val introduction: String?,
    @SerialName("location")
    val location: String,
    @SerialName("name")
    val name: String,
    @SerialName("profileImgUrl")
    val profileImgUrl: String?
)