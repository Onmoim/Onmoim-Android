package com.onmoim.core.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("birth")
    val birth: String,
    @SerialName("categoryList")
    val categoryList: List<Category>,
    @SerialName("id")
    val id: Int,
    @SerialName("introduction")
    val introduction: String?,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("locationName")
    val locationName: String,
    @SerialName("name")
    val name: String,
    @SerialName("profileImgUrl")
    val profileImgUrl: String?,
    @SerialName("gender")
    val gender: String,
    @SerialName("likedGroupsCount")
    val likedGroupsCount: Int,
    @SerialName("joinedGroupsCount")
    val joinedGroupsCount: Int,
    @SerialName("recentViewedGroupsCount")
    val recentViewedGroupsCount: Int,
) {

    @Serializable
    data class Category(
        @SerialName("categoryId")
        val categoryId: Int,
        @SerialName("categoryName")
        val categoryName: String,
    )
}