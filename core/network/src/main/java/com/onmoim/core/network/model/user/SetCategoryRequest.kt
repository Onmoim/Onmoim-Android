package com.onmoim.core.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetCategoryRequest(
    @SerialName("userId")
    val userId: Int,
    @SerialName("categoryIdList")
    val categoryIdList: List<Int>
)