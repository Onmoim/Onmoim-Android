package com.onmoim.core.data.repository

import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.model.HomeGroup
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.GroupApi
import com.onmoim.core.network.model.group.CreateGroupRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
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
}