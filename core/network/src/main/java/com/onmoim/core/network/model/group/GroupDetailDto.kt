package com.onmoim.core.network.model.group


import com.onmoim.core.network.model.MeetingDetailDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupDetailDto(
    @SerialName("address")
    val address: String,
    @SerialName("category")
    val category: String,
    @SerialName("description")
    val description: String,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("list")
    val list: List<MeetingDetailDto>,
    @SerialName("memberCount")
    val memberCount: Int,
    @SerialName("title")
    val title: String,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("status")
    val status: String
)