package com.onmoim.core.data.repository

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.datastore.PreferencesKey
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : TokenRepository {
    override suspend fun getAccessToken(): String? {
        return withContext(ioDispatcher) {
            dataStorePreferences.getString(PreferencesKey.ACCESS_TOKEN).first()
        }
    }

    override suspend fun getRefreshToken(): String? {
        return withContext(ioDispatcher) {
            dataStorePreferences.getString(PreferencesKey.REFRESH_TOKEN).first()
        }
    }

    override suspend fun setJwt(accessToken: String, refreshToken: String?) {
        withContext(ioDispatcher) {
            dataStorePreferences.putString(PreferencesKey.ACCESS_TOKEN, accessToken)
            refreshToken?.let {
                dataStorePreferences.putString(PreferencesKey.REFRESH_TOKEN, it)
            }
        }
    }

    override suspend fun clearJwt() {
        withContext(ioDispatcher) {
            dataStorePreferences.removeString(PreferencesKey.ACCESS_TOKEN)
            dataStorePreferences.removeString(PreferencesKey.REFRESH_TOKEN)
        }
    }
}