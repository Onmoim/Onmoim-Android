package com.onmoim.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onmoim.core.data.model.Account
import com.onmoim.core.data.model.Group
import com.onmoim.core.data.model.Profile
import com.onmoim.core.data.pagingsource.RecentGroupPagingSource
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.UserApi
import com.onmoim.core.network.model.user.SetCategoryRequest
import com.onmoim.core.network.model.user.SignUpRequest
import com.onmoim.core.network.model.user.UpdateProfileRequestDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun signUp(
        locationId: Int,
        birth: String,
        gender: String,
        name: String
    ): Account {
        val req = SignUpRequest(
            locationId = locationId,
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
                gender = data.gender,
                birth = birthDateTime.toLocalDate(),
                introduction = data.introduction,
                interestCategories = data.categoryList.map {
                    Profile.Category(
                        id = it.categoryId,
                        name = it.categoryName
                    )
                },
                locationId = data.locationId,
                location = data.locationName,
                profileImgUrl = data.profileImgUrl,
                favoriteGroupsCount = data.likedGroupsCount,
                recentViewedGroupsCount = data.recentViewedGroupsCount,
                joinedGroupsCount = data.joinedGroupsCount
            )

            emit(profile)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)

    override suspend fun withdrawal(id: Int): Result<Unit> {
        val resp = withContext(ioDispatcher) {
            userApi.withdrawal(id)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }

    override fun getRecentGroupPagingData(size: Int): Flow<PagingData<Group>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecentGroupPagingSource(userApi) }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun updateProfile(
        id: Int,
        name: String,
        birth: String,
        gender: String,
        locationId: Int,
        introduction: String,
        categoryIds: List<Int>,
        originImageUrl: String?,
        imagePath: String?
    ): Result<Unit> {
        val updateProfileRequestDto = UpdateProfileRequestDto(
            name = name,
            birth = birth,
            gender = gender,
            locationId = locationId,
            introduction = introduction,
            categoryIdList = categoryIds,
            profileImgUrl = when {
                originImageUrl != null && imagePath == null -> originImageUrl
                originImageUrl != null && imagePath != null -> originImageUrl
                originImageUrl == null && imagePath != null -> null
                else -> null
            }
        )
        val requestBodyJson = Json.encodeToString(updateProfileRequestDto)
        val requestBody = requestBodyJson.toRequestBody("application/json".toMediaTypeOrNull())

        val imageFile = imagePath?.let { File(it) }
        val imageFilePart = imageFile?.let {
            MultipartBody.Part.createFormData(
                "image",
                it.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        val resp = withContext(ioDispatcher) {
            userApi.updateProfile(id, requestBody, imageFilePart)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }
}