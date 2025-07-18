package com.onmoim.core.data.repository

import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.model.HomeGroup
import com.onmoim.core.network.api.GroupApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupApi: GroupApi
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
    }
}