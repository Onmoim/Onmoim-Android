package com.onmoim.core.data.model

import java.time.LocalDate

data class Profile(
    val id: Int,
    val name: String,
    val gender: String,
    val birth: LocalDate,
    val introduction: String?,
    val interestCategories: List<Category>,
    val locationId: Int,
    val location: String,
    val profileImgUrl: String?,
    val favoriteGroupsCount: Int,
    val recentViewedGroupsCount: Int,
    val joinedGroupsCount: Int
) {
    data class Category(
        val id: Int,
        val name: String
    )
}