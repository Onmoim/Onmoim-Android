package com.onmoim.core.data.model

import com.onmoim.core.constant.AccountStatus

data class Account(
    val jwt: Jwt,
    val status: AccountStatus
) {
    companion object {
        fun create(
            accessToken: String,
            refreshToken: String?,
            accountStatus: String?
        ): Account {
            val jwt = Jwt(accessToken, refreshToken)
            val status = when (accountStatus) {
                "EXISTS" -> AccountStatus.EXISTS
                "NO_CATEGORY" -> AccountStatus.NO_CATEGORY
                else -> AccountStatus.NOT_EXISTS
            }
            return Account(jwt, status)
        }
    }
}
