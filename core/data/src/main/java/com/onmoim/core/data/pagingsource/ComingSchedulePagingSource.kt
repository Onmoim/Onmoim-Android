package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.constant.UpcomingMeetingsFilter
import com.onmoim.core.data.model.Meeting
import com.onmoim.core.network.api.MeetingApi
import java.time.LocalDateTime

class ComingSchedulePagingSource(
    private val meetingApi: MeetingApi,
    private val filters: Set<UpcomingMeetingsFilter>,
    private val groupId: Int?
) : PagingSource<Map<String, Any?>, Meeting>() {
    companion object {
        private const val NEXT_KEY_CURSOR_START_AT = "cursorStartAt"
        private const val NEXT_KEY_CURSOR_ID = "cursorId"
    }

    override fun getRefreshKey(state: PagingState<Map<String, Any?>, Meeting>): Map<String, Any?>? {
        return null
    }

    override suspend fun load(params: LoadParams<Map<String, Any?>>): LoadResult<Map<String, Any?>, Meeting> {
        return try {
            val key = params.key
            val resp = meetingApi.getUpcomingMeetings(
//                groupId = groupId,  // FIXME: api 수정되면 확인
                thisWeekYn = if (filters.contains(UpcomingMeetingsFilter.WEEK)) true else null,
                thisMonthYn = if (filters.contains(UpcomingMeetingsFilter.MONTH)) true else null,
                joinedYn = if (filters.contains(UpcomingMeetingsFilter.JOINED)) true else null,
                regularYn = if (filters.contains(UpcomingMeetingsFilter.REGULAR)) true else null,
                flashYn = if (filters.contains(UpcomingMeetingsFilter.FLASH)) true else null,
                cursorStartAt = key?.get(NEXT_KEY_CURSOR_START_AT) as? String,
                cursorId = key?.get(NEXT_KEY_CURSOR_ID) as? Int,
                size = params.loadSize
            )
            val data = resp.body()?.data
            val meetings = data?.content?.map {
                Meeting(
                    id = it.id,
                    groupId = it.groupId,
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
                    latitude = it.location.y,
                    longitude = it.location.x,
                    attendance = it.attendance
                )
            } ?: emptyList()
            val nextCursorStartAt = data?.nextCursorStartAt
            val nextCursorId = data?.nextCursorId
            val nextKey = if (data?.hasNext == true) {
                mapOf<String, Any?>(
                    NEXT_KEY_CURSOR_START_AT to nextCursorStartAt,
                    NEXT_KEY_CURSOR_ID to nextCursorId
                )
            } else {
                null
            }
            LoadResult.Page(data = meetings, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("ComingSchedulePagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}