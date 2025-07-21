package com.onmoim.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasePageDto<T>(
    @SerialName("content")
    val content: List<T>,
    @SerialName("extraInfo")
    val extraInfo: ExtraInfoDto,
)
