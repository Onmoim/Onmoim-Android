package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Account

interface UserRepository {
    suspend fun signUp(
        addressId: Int,
        birth: String,
        gender: String,
        name: String
    ): Account

    suspend fun setUserId(id: Int)
    suspend fun getUserId(): Int?
    suspend fun clearUserId()
}