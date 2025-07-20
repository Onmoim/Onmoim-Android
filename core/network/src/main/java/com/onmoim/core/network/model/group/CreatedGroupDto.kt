package com.onmoim.core.network.model.group


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatedGroupDto(
    @SerialName("creatorId")
    val creatorId: Int,
    @SerialName("description")
    val description: String,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("memberCount")
    val memberCount: Int,
    @SerialName("name")
    val name: String,
    @SerialName("subscribeDestination")
    val subscribeDestination: String
)