package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Account
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.AuthApi
import com.onmoim.core.network.model.auth.SignInRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {
    override fun signIn(
        provider: String,
        token: String
    ): Flow<Account> = flow {
        val reqBody = SignInRequest(provider, token)
        val resp = authApi.signIn(reqBody)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val account = Account.create(data.accessToken, data.refreshToken, data.status)
            emit(account)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)
}