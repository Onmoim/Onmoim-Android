package com.onmoim.core.data.repository

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppSettingRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : AppSettingRepository {
    override suspend fun setUserId(id: Int) {
        withContext(ioDispatcher) {
            dataStorePreferences.putInt(USER_ID_KEY, id)
        }
    }

    override suspend fun getUserId(): Int? {
        return withContext(ioDispatcher) {
            dataStorePreferences.getInt(USER_ID_KEY).first()
        }
    }

    override suspend fun clearUserId() {
        withContext(ioDispatcher) {
            dataStorePreferences.removeInt(USER_ID_KEY)
        }
    }

    override suspend fun setHasNotInterest(value: Boolean) {
        withContext(ioDispatcher) {
            dataStorePreferences.putBoolean(HAS_NOT_INTEREST_KEY, value)
        }
    }

    override suspend fun hasNotInterest(): Boolean {
        return withContext(ioDispatcher) {
            dataStorePreferences.getBoolean(HAS_NOT_INTEREST_KEY).first() ?: true
        }
    }

    override suspend fun clearHasNotInterest() {
        withContext(ioDispatcher) {
            dataStorePreferences.removeBoolean(HAS_NOT_INTEREST_KEY)
        }
    }

    override suspend fun setUserLocation(locationId: Int, locationName: String) {
        withContext(ioDispatcher) {
            dataStorePreferences.putInt(USER_LOCATION_ID_KEY, locationId)
            dataStorePreferences.putString(USER_LOCATION_NAME_KEY, locationName)
        }
    }

    override fun getUserLocationFlow(): Flow<Pair<Int, String>?> = combine(
        dataStorePreferences.getInt(USER_LOCATION_ID_KEY),
        dataStorePreferences.getString(USER_LOCATION_NAME_KEY)
    ) { locationId, locationName ->
        if (locationId != null && locationName != null) {
            Pair(locationId, locationName)
        } else {
            null
        }
    }.flowOn(ioDispatcher)

    override suspend fun clearUserLocation() {
        withContext(ioDispatcher) {
            dataStorePreferences.removeInt(USER_LOCATION_ID_KEY)
            dataStorePreferences.removeString(USER_LOCATION_NAME_KEY)
        }
    }

    companion object {
        private const val USER_ID_KEY = "userId"
        private const val HAS_NOT_INTEREST_KEY = "hasNotInterest"
        private const val USER_LOCATION_ID_KEY = "userLocationId"
        private const val USER_LOCATION_NAME_KEY = "userLocationName"
    }
}