package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.model.Account
import com.onmoim.core.data.model.Group
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
    fun getRecentGroupPagingData(size: Int = 20): Flow<PagingData<Group>>
    suspend fun updateProfile(
        id: Int,
        name: String,
        birth: String,
        gender: String,
        locationId: Int,
        introduction: String,
        categoryIds: List<Int>,
        originImageUrl: String?,
        imagePath: String?
    ): Result<Unit>
}