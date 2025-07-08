package com.onmoim.core.data.model

import com.onmoim.core.data.constant.AccountStatus

data class Account(
    val jwt: Jwt,
    val status: AccountStatus,
    val userId: Int?,
) {
    companion object {
        fun create(
            accessToken: String,
            refreshToken: String?,
            accountStatus: String?,
            userId: Int? = null
        ): Account {
            val jwt = Jwt(accessToken, refreshToken)
            val status = when (accountStatus) {
                "EXISTS" -> AccountStatus.EXISTS
                "NO_CATEGORY" -> AccountStatus.NO_CATEGORY
                else -> AccountStatus.NOT_EXISTS
            }
            return Account(jwt, status, userId)
        }
    }
}
