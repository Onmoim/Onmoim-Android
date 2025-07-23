package com.onmoim.core.network.model.meeting

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseMeetingPageDto <T> (
    @SerialName("content")
    val content: List<T>,
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("nextCursorId")
    val nextCursorId: Int?
)
