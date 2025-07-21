package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.data.model.GroupDetail
import com.onmoim.core.data.model.HomeGroup
import com.onmoim.core.data.model.Member
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
    suspend fun deleteGroup(id: Int): Result<Unit>
    suspend fun favoriteGroup(id: Int): Result<Unit>
    fun getActiveStatistics(id: Int): Flow<ActiveStatistics>
    fun getGroupMemberPagingData(id: Int, size: Int = 20): Flow<PagingData<Member>>
}