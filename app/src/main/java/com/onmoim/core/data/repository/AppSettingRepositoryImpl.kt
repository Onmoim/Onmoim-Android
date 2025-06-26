package com.onmoim.core.data.repository

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
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

    companion object {
        private const val USER_ID_KEY = "userId"
        private const val HAS_NOT_INTEREST_KEY = "hasNotInterest"
    }
}