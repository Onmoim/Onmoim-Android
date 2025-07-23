package com.onmoim.core.network.model.group

import com.onmoim.core.network.model.ExtraInfoDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseGroupPageDto<T>(
    @SerialName("content")
    val content: List<T>,
    @SerialName("extraInfo")
    val extraInfo: ExtraInfoDto,
)