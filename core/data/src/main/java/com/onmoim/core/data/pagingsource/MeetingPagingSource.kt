package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.model.Meeting
import com.onmoim.core.network.api.MeetingApi
import java.time.LocalDateTime

class MeetingPagingSource(
    private val meetingApi: MeetingApi,
    private val groupId: Int,
    private val filter: MeetingType?
) : PagingSource<Int, Meeting>() {
    override fun getRefreshKey(state: PagingState<Int, Meeting>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Meeting> {
        return try {
            val key = params.key
            val resp = meetingApi.getMeetings(
                groupId = groupId,
                cursorId = key,
                size = params.loadSize,
                type = when (filter) {
                    MeetingType.REGULAR -> "REGULAR"
                    MeetingType.LIGHTNING -> "FLASH"
                    else -> null
                }
            )
            val data = resp.body()?.data
            val meetings = data?.content?.map {
                Meeting(
                    id = it.id,
                    title = it.title,
                    placeName = it.placeName,
                    startDate = LocalDateTime.parse(it.startAt),
                    cost = it.cost,
                    joinCount = it.joinCount,
                    capacity = it.capacity,
                    type = when (it.type) {
                        "REGULAR" -> MeetingType.REGULAR
                        "FLASH" -> MeetingType.LIGHTNING
                        else -> MeetingType.REGULAR
                    },
                    imgUrl = it.imgUrl,
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            } ?: emptyList()
            val nextCursorId = data?.nextCursorId
            val nextKey = if (data?.hasNext == true) nextCursorId else null
            LoadResult.Page(data = meetings, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("MeetingPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}