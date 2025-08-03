package com.onmoim.core.data.model

import com.onmoim.core.data.constant.SystemMessageType

data class SystemMessage(
    val type: SystemMessageType,
    val message: String
)
