package com.onmoim.core.data.repository

import com.onmoim.core.constant.AccountStatus
import com.onmoim.core.data.model.Account
import com.onmoim.core.data.model.Jwt
import com.onmoim.core.network.api.AuthApi
import com.onmoim.core.network.model.auth.SignInRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {
    override fun signIn(
        provider: String,
        token: String
    ): Flow<Account> = flow {
        val reqBody = SignInRequest(provider, token)
        val resp = authApi.signIn(reqBody)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val jwt = Jwt(data.accessToken, data.refreshToken)
            val accountStatus = when (data.status) {
                "EXISTS" -> AccountStatus.EXISTS
                "NO_CATEGORY" -> AccountStatus.NO_CATEGORY
                else -> AccountStatus.NOT_EXISTS
            }
            val account = Account(jwt, accountStatus)
            emit(account)
        } else {
            throw HttpException(resp)
        }
    }
}