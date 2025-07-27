package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.constant.HomeRecommend
import com.onmoim.core.data.constant.JoinGroupResult
import com.onmoim.core.data.model.ActiveStatistics
import com.onmoim.core.data.model.Group
import com.onmoim.core.data.model.GroupDetail
import com.onmoim.core.data.model.Member
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getHomePopularGroups(homePopular: HomePopular): Flow<List<Group>>
    fun getHomePopularGroupPagingData(
        homePopular: HomePopular,
        size: Int = 20
    ): Flow<PagingData<Group>>

    fun getHomeRecommendGroups(homeRecommend: HomeRecommend): Flow<List<Group>>
    fun getHomeRecommendGroupPagingData(
        homeRecommend: HomeRecommend,
        size: Int = 20
    ): Flow<PagingData<Group>>

    fun getFavoriteGroupPagingData(size: Int = 20): Flow<PagingData<Group>>
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
    suspend fun banMember(groupId: Int, memberId: Int): Result<Unit>
    suspend fun transferGroupOwner(groupId: Int, memberId: Int): Result<Unit>
    suspend fun updateGroup(
        groupId: Int,
        description: String,
        capacity: Int,
        imageUrl: String? = null
    ): Result<Unit>

    suspend fun joinGroup(groupId: Int): Result<JoinGroupResult>
    fun getJoinedGroups(size: Int = 10): Flow<List<Group>>
    fun getJoinedGroupPagingData(size: Int = 20): Flow<PagingData<Group>>
}