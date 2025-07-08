package com.onmoim.core.data.model

import java.time.LocalDate

data class Profile(
    val id: Int,
    val name: String,
    val birth: LocalDate,
    val introduction: String?,
    val interests: List<String>,
    val location: String,
    val profileImgUrl: String?
)
