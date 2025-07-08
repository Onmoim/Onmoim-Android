package com.onmoim.core.data.repository

interface AppSettingRepository {
    suspend fun setUserId(id: Int)
    suspend fun getUserId(): Int?
    suspend fun clearUserId()
    suspend fun setHasNotInterest(value: Boolean)
    suspend fun hasNotInterest(): Boolean
    suspend fun clearHasNotInterest()
}