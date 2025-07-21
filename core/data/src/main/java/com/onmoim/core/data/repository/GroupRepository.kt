package com.onmoim.core.data.repository

import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.model.GroupDetail
import com.onmoim.core.data.model.HomeGroup
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getHomePopularGroups(homePopular: HomePopular): Flow<List<HomeGroup>>
    fun createGroup(
        name: String,
        description: String,
        locationId: Int,
        categoryId: Int,
        capacity: Int
    ): Flow<Int>
    fun getGroupDetail(id: Int): Flow<GroupDetail>
    suspend fun leaveGroup(id: Int): Result<Unit>
}