package com.onmoim.core.network.model.post


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentThreadDto(
    @SerialName("hasMore")
    val hasMore: Boolean,
    @SerialName("nextCursor")
    val nextCursor: Int?,
    @SerialName("parentComment")
    val parentComment: ParentComment,
    @SerialName("replies")
    val replies: List<Reply>
) {
    @Serializable
    data class ParentComment(
        @SerialName("authorName")
        val authorName: String,
        @SerialName("authorProfileImg")
        val authorProfileImg: String,
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

    @Serializable
    data class Reply(
        @SerialName("authorName")
        val authorName: String,
        @SerialName("authorProfileImg")
        val authorProfileImg: String,
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
}