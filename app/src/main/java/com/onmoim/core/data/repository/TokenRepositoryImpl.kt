package com.onmoim.core.data.repository

import com.onmoim.core.datastore.DataStorePreferences
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
            dataStorePreferences.getString(ACCESS_TOKEN_KEY).first()
        }
    }

    override suspend fun getRefreshToken(): String? {
        return withContext(ioDispatcher) {
            dataStorePreferences.getString(REFRESH_TOKEN_KEY).first()
        }
    }

    override suspend fun setJwt(accessToken: String, refreshToken: String?) {
        withContext(ioDispatcher) {
            dataStorePreferences.putString(ACCESS_TOKEN_KEY, accessToken)
            refreshToken?.let {
                dataStorePreferences.putString(REFRESH_TOKEN_KEY, it)
            }
        }
    }

    override suspend fun clearJwt() {
        withContext(ioDispatcher) {
            dataStorePreferences.removeString(ACCESS_TOKEN_KEY)
            dataStorePreferences.removeString(REFRESH_TOKEN_KEY)
        }
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "accessToken"
        private const val REFRESH_TOKEN_KEY = "refreshToken"
    }
}