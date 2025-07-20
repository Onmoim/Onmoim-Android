package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Account
import com.onmoim.core.data.model.Profile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signUp(
        locationId: Int,
        birth: String,
        gender: String,
        name: String
    ): Account

    suspend fun setInterest(userId: Int, interestIds: List<Int>): Result<Unit>

    fun getMyProfile(): Flow<Profile>
    suspend fun withdrawal(id: Int): Result<Unit>
}