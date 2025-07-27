package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.Group
import com.onmoim.core.network.api.GroupApi

class PopularGroupPagingSource(
    private val groupApi: GroupApi,
    private val homePopular: HomePopular
) : PagingSource<Int, Group>() {
    override fun getRefreshKey(state: PagingState<Int, Group>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Group> {
        return try {
            val key = params.key
            val resp = when (homePopular) {
                HomePopular.NEARBY -> groupApi.getPopularNearbyGroups(
                    lastGroupIdx = key,
                    requestSize = params.loadSize
                )

                HomePopular.ACTIVE -> groupApi.getPopularActiveGroups(
                    lastGroupIdx = key,
                    requestSize = params.loadSize
                )
            }
            val data = resp.body()?.data
            val groups = data?.content?.map {
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
            } ?: emptyList()
            val lastGroupId = data?.extraInfo?.lastGroupId
            val nextKey = if (data?.extraInfo?.hasNext == true) lastGroupId else null
            LoadResult.Page(data = groups, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("PopularGroupPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}