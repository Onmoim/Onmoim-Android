package com.onmoim.core.network.model.post


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    @SerialName("authorId")
    val authorId: Int,
    @SerialName("authorName")
    val authorName: String,
    @SerialName("authorProfileImage")
    val authorProfileImage: String?,
    @SerialName("content")
    val content: String,
    @SerialName("createdDate")
    val createdDate: String,
    @SerialName("groupId")
    val groupId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("imageUrls")
    val imageUrls: List<String>,
    @SerialName("isLiked")
    val isLiked: Boolean,
    @SerialName("likeCount")
    val likeCount: Int,
    @SerialName("modifiedDate")
    val modifiedDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String
)