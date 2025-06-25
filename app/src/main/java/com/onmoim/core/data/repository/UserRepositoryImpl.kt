package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Account
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.UserApi
import com.onmoim.core.network.model.user.SignUpRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun signUp(
        addressId: Int,
        birth: String,
        gender: String,
        name: String
    ): Account {
        val req = SignUpRequest(
            addressId = addressId,
            birth = birth,
            gender = gender,
            name = name
        )
        val resp = withContext(ioDispatcher) {
            userApi.signUp(req)
        }
        val data = resp.body()

        if (resp.isSuccessful && data != null) {
            return Account.create(data.accessToken, data.refreshToken, data.status)
        } else {
            throw HttpException(resp)
        }
    }
}