package com.onmoim.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.constant.HomeRecommend
import com.onmoim.core.data.constant.JoinGroupResult
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.data.model.Group
import com.onmoim.core.data.model.GroupDetail
import com.onmoim.core.data.model.MeetingDetail
import com.onmoim.core.data.model.Member
import com.onmoim.core.data.pagingsource.GroupMemberPagingSource
import com.onmoim.core.data.pagingsource.LikedGroupPagingSource
import com.onmoim.core.data.pagingsource.PopularGroupPagingSource
import com.onmoim.core.data.pagingsource.RecommendGroupPagingSource
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.GroupApi
import com.onmoim.core.network.model.group.CreateGroupRequest
import com.onmoim.core.network.model.group.MemberIdRequestDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupApi: GroupApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : GroupRepository {
    override fun getHomePopularGroups(homePopular: HomePopular): Flow<List<Group>> = flow {
        val resp = when (homePopular) {
            HomePopular.NEARBY -> groupApi.getPopularNearbyGroups()
            HomePopular.ACTIVE -> groupApi.getPopularActiveGroups()
        }
        val data = resp.body()?.data?.content

        if (resp.isSuccessful && data != null) {
            val groups = data.map {
                Group(
                    id = it.groupId,
                    imageUrl = it.imageUrl,
                    title = it.name,
                    location = it.dong,
                    memberCount = it.memberCount,
                    scheduleCount = it.upcomingMeetingCount,
                    categoryName = it.category,
                    memberStatus = when {
                        it.status.contains("OWNER") -> MemberStatus.OWNER
                        it.status.contains("MEMBER") -> MemberStatus.MEMBER
                        it.status.contains("BAN") -> MemberStatus.BAN
                        else -> MemberStatus.NONE
                    },
                    isFavorite = it.likeStatus.contains("LIKE"),
                    isRecommend = false
                )
            }
            emit(groups)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)

    override fun getHomePopularGroupPagingData(
        homePopular: HomePopular,
        size: Int
    ): Flow<PagingData<Group>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PopularGroupPagingSource(groupApi, homePopular) }
        ).flow.flowOn(ioDispatcher)
    }

    override fun getHomeRecommendGroups(homeRecommend: HomeRecommend): Flow<List<Group>> =
        flow {
            val resp = when (homeRecommend) {
                HomeRecommend.CATEGORY -> groupApi.getRecommendCategoryGroups()
                HomeRecommend.LOCATION -> groupApi.getRecommendLocationGroups()
            }
            val data = resp.body()?.data?.content

            if (resp.isSuccessful && data != null) {
                val groups = data.map {
                    Group(
                        id = it.groupId,
                        imageUrl = it.imgUrl,
                        title = it.name,
                        location = it.location,
                        memberCount = it.memberCount,
                        scheduleCount = it.upcomingMeetingCount,
                        categoryName = it.category,
                        memberStatus = when {
                            it.status.contains("OWNER") -> MemberStatus.OWNER
                            it.status.contains("MEMBER") -> MemberStatus.MEMBER
                            it.status.contains("BAN") -> MemberStatus.BAN
                            else -> MemberStatus.NONE
                        },
                        isFavorite = it.likeStatus.contains("LIKE"),
                        isRecommend = it.recommendStatus.contains("RECOMMEND")
                    )
                }
                emit(groups)
            } else {
                throw HttpException(resp)
            }
        }.flowOn(ioDispatcher)

    override fun getHomeRecommendGroupPagingData(
        homeRecommend: HomeRecommend,
        size: Int
    ): Flow<PagingData<Group>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecommendGroupPagingSource(groupApi, homeRecommend) }
        ).flow.flowOn(ioDispatcher)
    }

    override fun getFavoriteGroupPagingData(size: Int): Flow<PagingData<Group>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { LikedGroupPagingSource(groupApi) }
        ).flow.flowOn(ioDispatcher)
    }

    override fun createGroup(
        name: String,
        description: String,
        locationId: Int,
        categoryId: Int,
        capacity: Int
    ): Flow<Int> = flow {
        val createGroupRequest = CreateGroupRequest(
            name = name,
            description = description,
            locationId = locationId,
            categoryId = categoryId,
            capacity = capacity
        )
        val resp = groupApi.createGroup(createGroupRequest)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            emit(data.groupId)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)

    override fun getGroupDetail(id: Int): Flow<GroupDetail> = flow {
        val resp = groupApi.getGroupDetail(id)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val groupDetail = GroupDetail(
                id = data.groupId,
                title = data.title,
                imageUrl = data.imageUrl,
                location = data.address,
                category = data.category,
                categoryIconUrl = data.categoryIconUrl,
                memberCount = data.memberCount,
                description = data.description,
                meetingList = data.list.map {
                    MeetingDetail(
                        id = it.meetingId,
                        title = it.title,
                        placeName = it.placeName,
                        startDate = LocalDateTime.parse(it.startDate),
                        cost = it.cost,
                        joinCount = it.joinCount,
                        capacity = it.capacity,
                        attendance = it.attendance,
                        isLightning = it.type == "번개모임",
                        imgUrl = it.imgUrl,
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                },
                isFavorite = data.likeStatus.contains("LIKE"),
                memberStatus = when {
                    data.status.contains("OWNER") -> MemberStatus.OWNER
                    data.status.contains("MEMBER") -> MemberStatus.MEMBER
                    data.status.contains("BAN") -> MemberStatus.BAN
                    else -> MemberStatus.NONE
                },
                capacity = data.capacity
            )
            emit(groupDetail)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)

    override suspend fun leaveGroup(id: Int): Result<Unit> {
        val resp = withContext(ioDispatcher) {
            groupApi.leaveGroup(id)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }

    override suspend fun deleteGroup(id: Int): Result<Unit> {
        val resp = withContext(ioDispatcher) {
            groupApi.deleteGroup(id)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }

    override suspend fun favoriteGroup(id: Int): Result<Unit> {
        val resp = withContext(ioDispatcher) {
            groupApi.likeGroup(id)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }

    override fun getActiveStatistics(id: Int): Flow<ActiveStatistics> = flow {
        val resp = groupApi.getGroupStatistics(id)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val activeStatistics = ActiveStatistics(
                yearlyScheduleCount = data.annualSchedule,
                monthlyScheduleCount = data.monthlySchedule
            )
            emit(activeStatistics)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)

    override fun getGroupMemberPagingData(
        id: Int,
        size: Int
    ): Flow<PagingData<Member>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GroupMemberPagingSource(groupApi, id) }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun banMember(
        groupId: Int,
        memberId: Int
    ): Result<Unit> {
        val memberIdRequestDto = MemberIdRequestDto(memberId)
        val resp = withContext(ioDispatcher) {
            groupApi.banMember(groupId, memberIdRequestDto)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }

    override suspend fun transferGroupOwner(
        groupId: Int,
        memberId: Int
    ): Result<Unit> {
        val memberIdRequestDto = MemberIdRequestDto(memberId)
        val resp = withContext(ioDispatcher) {
            groupApi.transferGroupOwner(groupId, memberIdRequestDto)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }

    override suspend fun updateGroup(
        groupId: Int,
        description: String,
        capacity: Int,
        imageUrl: String?
    ): Result<Unit> {
        val requestMap: Map<String, Any> = mapOf(
            "description" to description,
            "capacity" to capacity
        )
        val requestJsonObject = JSONObject(requestMap)
        val requestBody =
            requestJsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val imageFile = imageUrl?.let { File(it) }
        val imageFilePart = imageFile?.let {
            MultipartBody.Part.createFormData(
                "file",
                it.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        val resp = withContext(ioDispatcher) {
            groupApi.updateGroup(groupId, requestBody, imageFilePart)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(HttpException(resp))
        }
    }

    override suspend fun joinGroup(groupId: Int): Result<JoinGroupResult> {
        val resp = withContext(ioDispatcher) {
            groupApi.joinGroup(groupId)
        }

        return when {
            resp.isSuccessful -> Result.success(JoinGroupResult.SUCCESS)
            resp.code() == 400 -> Result.success(JoinGroupResult.BANNED)
            resp.code() == 404 -> Result.success(JoinGroupResult.NOT_FOUND)
            resp.code() == 409 -> Result.success(JoinGroupResult.OVER_CAPACITY)
            else -> Result.failure(HttpException(resp))
        }
    }

    override fun getJoinedGroups(size: Int): Flow<List<Group>> =
        flow {
            val resp = groupApi.getJoinedGroups()
            val data = resp.body()?.data?.content

            if (resp.isSuccessful && data != null) {
                val groups = data.map {
                    Group(
                        id = it.groupId,
                        imageUrl = it.imgUrl,
                        title = it.name,
                        location = it.location,
                        memberCount = it.memberCount,
                        scheduleCount = it.upcomingMeetingCount,
                        categoryName = it.category,
                        memberStatus = when {
                            it.status.contains("OWNER") -> MemberStatus.OWNER
                            it.status.contains("MEMBER") -> MemberStatus.MEMBER
                            it.status.contains("BAN") -> MemberStatus.BAN
                            else -> MemberStatus.NONE
                        },
                        isFavorite = it.likeStatus.contains("LIKE"),
                        isRecommend = it.recommendStatus.contains("RECOMMEND")
                    )
                }
                emit(groups)
            } else {
                throw HttpException(resp)
            }
        }.flowOn(ioDispatcher)
}