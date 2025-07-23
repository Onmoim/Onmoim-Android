package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.model.Meeting
import kotlinx.coroutines.flow.Flow

interface MeetingRepository {
    fun getMeetingPagingData(
        groupId: Int,
        size: Int = 20,
        filter: MeetingType? = null
    ): Flow<PagingData<Meeting>>

    suspend fun deleteMeeting(
        groupId: Int,
        meetingId: Int
    ): Result<Unit>
}