package com.onmoim.core.data.repository

import kotlinx.coroutines.flow.Flow

interface AppSettingRepository {
    suspend fun setUserId(id: Int)
    suspend fun getUserId(): Int?
    suspend fun clearUserId()
    suspend fun setHasNotInterest(value: Boolean)
    suspend fun hasNotInterest(): Boolean
    suspend fun clearHasNotInterest()
    suspend fun setUserLocation(locationId: Int, locationName: String)
    fun getUserLocationFlow(): Flow<Pair<Int, String>?>
    suspend fun clearUserLocation()
}