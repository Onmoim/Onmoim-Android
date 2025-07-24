package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.model.Meeting
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

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

    suspend fun createMeeting(
        groupId: Int,
        type: String,
        title: String,
        startDateTime: LocalDateTime,
        placeName: String,
        latitude: Double,
        longitude: Double,
        capacity: Int,
        cost: Long,
        imagePath: String?
    ): Result<Unit>
}