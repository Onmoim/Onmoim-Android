package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Account
import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.UserApi
import com.onmoim.core.network.model.user.SetCategoryRequest
import com.onmoim.core.network.model.user.SignUpRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val dataStorePreferences: DataStorePreferences,
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
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            setUserId(data.userId)
            return Account.create(data.accessToken, data.refreshToken, data.status)
        } else {
            throw HttpException(resp)
        }
    }

    override suspend fun setInterest(
        userId: Int,
        interestIds: List<Int>
    ): Result<Unit> {
        return try {
            val setCategoryRequest = SetCategoryRequest(userId, interestIds)
            val resp = withContext(ioDispatcher) {
                userApi.setCategory(setCategoryRequest)
            }

            if (resp.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(HttpException(resp))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    companion object {
        private const val USER_ID_KEY = "userId"
    }
}