package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Account
import com.onmoim.core.data.model.Profile
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.UserApi
import com.onmoim.core.network.model.user.SetCategoryRequest
import com.onmoim.core.network.model.user.SignUpRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.time.LocalDateTime
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
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            return Account.create(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                accountStatus = data.status,
                userId = data.userId
            )
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

    override fun getMyProfile(): Flow<Profile> = flow {
        val resp = userApi.getMyProfile()
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val birthDateTime = LocalDateTime.parse(data.birth)
            val profile = Profile(
                id = data.id,
                name = data.name,
                birth = birthDateTime.toLocalDate(),
                introduction = data.introduction,
                interests = data.categoryList,
                location = data.location,
                profileImgUrl = data.profileImgUrl
            )

            emit(profile)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)
}