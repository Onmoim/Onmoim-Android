package com.onmoim.core.data.model

import com.onmoim.core.constant.AccountStatus

data class Account(
    val jwt: Jwt,
    val status: AccountStatus
)
