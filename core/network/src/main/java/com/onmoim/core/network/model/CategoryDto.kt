package com.onmoim.core.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("categoryId")
    val categoryId: Int,
    @SerialName("iconUrl")
    val iconUrl: String?,
    @SerialName("name")
    val name: String
)