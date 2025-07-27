package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.constant.MemberStatus
import com.onmoim.core.data.model.Group
import com.onmoim.core.network.api.UserApi

class RecentGroupPagingSource(
    private val userApi: UserApi
) : PagingSource<Map<String, Any?>, Group>() {
    companion object {
        private const val NEXT_KEY_CURSOR_VIEWED_AT = "cursorViewedAt"
        private const val NEXT_KEY_CURSOR_ID = "cursorId"
    }

    override fun getRefreshKey(state: PagingState<Map<String, Any?>, Group>): Map<String, Any?>? {
        return null
    }

    override suspend fun load(params: LoadParams<Map<String, Any?>>): LoadResult<Map<String, Any?>, Group> {
        return try {
            val key = params.key
            val resp = userApi.getRecentGroups(
                cursorViewedAt = key?.get(NEXT_KEY_CURSOR_VIEWED_AT) as? String,
                cursorId = key?.get(NEXT_KEY_CURSOR_ID) as? Int,
                size = params.loadSize
            )
            val data = resp.body()?.data
            val groups = data?.content?.map {
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
            val nextCursorViewedAt = data?.nextCursorViewedAt
            val nextCursorLogId = data?.nextCursorLogId
            val nextKey = if (data?.hasNext == true) {
                mapOf<String, Any?>(
                    NEXT_KEY_CURSOR_VIEWED_AT to nextCursorViewedAt,
                    NEXT_KEY_CURSOR_ID to nextCursorLogId
                )
            } else {
                null
            }
            LoadResult.Page(data = groups, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("RecentGroupPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}