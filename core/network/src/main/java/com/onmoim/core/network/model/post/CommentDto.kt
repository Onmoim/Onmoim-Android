package com.onmoim.core.network.model.post


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentDto(
    @SerialName("authorName")
    val authorName: String,
    @SerialName("authorProfileImg")
    val authorProfileImg: String?,
    @SerialName("content")
    val content: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("id")
    val id: Int,
    @SerialName("replyCount")
    val replyCount: Int,
    @SerialName("updatedAt")
    val updatedAt: String
)