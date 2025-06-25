package com.onmoim.core.data.repository

import com.onmoim.core.constant.AccountStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signIn(provider: String, token: String): Flow<AccountStatus>
}