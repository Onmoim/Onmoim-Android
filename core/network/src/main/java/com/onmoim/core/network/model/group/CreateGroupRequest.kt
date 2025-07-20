package com.onmoim.core.network.model.group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateGroupRequest(
    @SerialName("capacity")
    val capacity: Int,
    @SerialName("categoryId")
    val categoryId: Int,
    @SerialName("description")
    val description: String,
    @SerialName("locationId")
    val locationId: Int,
    @SerialName("name")
    val name: String
)