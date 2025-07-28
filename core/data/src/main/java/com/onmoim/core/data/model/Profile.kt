package com.onmoim.core.data.model

import java.time.LocalDate

data class Profile(
    val id: Int,
    val name: String,
    val gender: String,
    val birth: LocalDate,
    val introduction: String?,
    val interestCategories: List<String>,
    val interestCategoryIds: List<Int>,
    val locationId: Int,
    val location: String,
    val profileImgUrl: String?
)
