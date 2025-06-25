package com.onmoim.core.data.repository

import com.onmoim.core.constant.AccountStatus
import com.onmoim.core.data.PreferencesKey
import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.network.api.AuthApi
import com.onmoim.core.network.model.auth.SignInRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val dataStorePreferences: DataStorePreferences
) : AuthRepository {
    override fun signIn(
        provider: String,
        token: String
    ): Flow<AccountStatus> = flow {
        val reqBody = SignInRequest(provider, token)
        val resp = authApi.signIn(reqBody)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            setJwt(data.accessToken, data.refreshToken)

            val accountStatus = when (data.status) {
                "EXISTS" -> AccountStatus.EXISTS
                "NO_CATEGORY" -> AccountStatus.NO_CATEGORY
                else -> AccountStatus.NOT_EXISTS
            }
            emit(accountStatus)
        } else {
            throw HttpException(resp)
        }
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