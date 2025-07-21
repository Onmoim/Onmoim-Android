package com.onmoim.core.data.repository

import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.GroupDetail
import com.onmoim.core.data.model.HomeGroup
import com.onmoim.core.data.model.MeetingDetail
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.GroupApi
import com.onmoim.core.network.model.group.CreateGroupRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.time.LocalDateTime
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupApi: GroupApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : GroupRepository {
    override fun getHomePopularGroups(homePopular: HomePopular): Flow<List<HomeGroup>> = flow {
        val resp = when (homePopular) {
            HomePopular.NEARBY -> groupApi.getPopularNearbyGroups()
            HomePopular.ACTIVE -> groupApi.getPopularActiveGroups()
        }
        val data = resp.body()?.data?.content

        if (resp.isSuccessful && data != null) {
            val groups = data.map {
                HomeGroup(
                    id = it.groupId,
                    imageUrl = it.imageUrl,
                    title = it.name,
                    location = it.dong,
                    memberCount = it.memberCount,
                    scheduleCount = it.upcomingMeetingCount,
                    categoryName = it.category
                )
            }
            emit(groups)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)

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
                memberCount = data.memberCount,
                description = data.description,
                meetingList = data.list.map {
                    MeetingDetail(
                        id = 0, // FIXME: 확인 후 수정
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
                isFavorite = data.status.contains("BOOKMARK"),
                memberStatus = when {
                    data.status.contains("OWNER") -> MemberStatus.OWNER
                    data.status.contains("MEMBER") -> MemberStatus.MEMBER
                    data.status.contains("BAN") -> MemberStatus.BAN
                    else -> MemberStatus.NONE
                }
            )
            emit(groupDetail)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)
}