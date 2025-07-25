package com.onmoim.core.network.model.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasePostPageDto <T> (
    @SerialName("content")
    val content: List<T>,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursorId")
    val nextCursorId: Int?
)
