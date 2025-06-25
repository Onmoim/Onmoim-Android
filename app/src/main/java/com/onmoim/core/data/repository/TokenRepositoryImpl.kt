package com.onmoim.core.data.repository

import com.onmoim.core.data.PreferencesKey
import com.onmoim.core.datastore.DataStorePreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
) : TokenRepository {
    override suspend fun getAccessToken(): String? {
        return dataStorePreferences.getString(PreferencesKey.ACCESS_TOKEN).first()
    }

    override suspend fun getRefreshToken(): String? {
        return dataStorePreferences.getString(PreferencesKey.REFRESH_TOKEN).first()
    }

    override suspend fun setJwt(accessToken: String, refreshToken: String?) {
        dataStorePreferences.putString(PreferencesKey.ACCESS_TOKEN, accessToken)
        refreshToken?.let {
            dataStorePreferences.putString(PreferencesKey.REFRESH_TOKEN, it)
        }
    }

    override suspend fun clearJwt() {
        dataStorePreferences.remove(PreferencesKey.ACCESS_TOKEN)
        dataStorePreferences.remove(PreferencesKey.REFRESH_TOKEN)
    }
}