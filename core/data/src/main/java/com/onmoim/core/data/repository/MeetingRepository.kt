package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.JoinMeetingResult
import com.onmoim.core.data.constant.LeaveMeetingResult
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.constant.UpcomingMeetingsFilter
import com.onmoim.core.data.model.Meeting
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
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

    suspend fun joinMeeting(groupId: Int, meetingId: Int): Result<JoinMeetingResult>
    suspend fun leaveMeeting(groupId: Int, meetingId: Int): Result<LeaveMeetingResult>
    fun getUpcomingMeetingsByDate(date: LocalDate): Flow<List<Meeting>>
    fun getUpcomingMeetingPagingData(
        filters: Set<UpcomingMeetingsFilter>,
        groupId: Int? = null,
        size: Int = 20
    ): Flow<PagingData<Meeting>>
}