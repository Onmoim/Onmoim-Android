package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Account
import com.onmoim.core.data.model.Profile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun signUp(
        addressId: Int,
        birth: String,
        gender: String,
        name: String
    ): Account

    suspend fun setInterest(userId: Int, interestIds: List<Int>): Result<Unit>

    fun getMyProfile(): Flow<Profile>

    suspend fun setUserId(id: Int)
    suspend fun getUserId(): Int?
    suspend fun clearUserId()
    suspend fun setHasNotInterest(value: Boolean)
    suspend fun hasNotInterest(): Boolean
    suspend fun clearHasNotInterest()
}