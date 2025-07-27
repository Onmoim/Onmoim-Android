package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.Group
import com.onmoim.core.network.api.GroupApi

class JoinedGroupPagingSource(
    private val groupApi: GroupApi,
): PagingSource<Int, Group>() {
    override fun getRefreshKey(state: PagingState<Int, Group>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Group> {
        return try {
            val key = params.key
            val resp = groupApi.getJoinedGroups(
                cursorId = key,
                size = params.loadSize
            )
            val data = resp.body()?.data
            val members = data?.content?.map {
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
            } ?: emptyList()
            val nextCursorId = data?.nextCursorId
            val nextKey = if (data?.hasNext == true) nextCursorId else null
            LoadResult.Page(data = members, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("JoinedGroupPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}