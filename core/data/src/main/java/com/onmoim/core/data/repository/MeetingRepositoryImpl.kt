package com.onmoim.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onmoim.core.data.constant.JoinMeetingResult
import com.onmoim.core.data.constant.LeaveMeetingResult
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.constant.UpcomingMeetingsFilter
import com.onmoim.core.data.model.Meeting
import com.onmoim.core.data.pagingsource.ComingSchedulePagingSource
import com.onmoim.core.data.pagingsource.MeetingPagingSource
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.MeetingApi
import com.onmoim.core.network.model.meeting.CreateMeetingRequestDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val meetingApi: MeetingApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : MeetingRepository {
    override fun getMeetingPagingData(
        groupId: Int,
        size: Int,
        filter: MeetingType?
    ): Flow<PagingData<Meeting>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MeetingPagingSource(meetingApi, groupId, filter) }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun deleteMeeting(
        groupId: Int,
        meetingId: Int
    ): Result<Unit> {
        val resp = withContext(ioDispatcher) {
            meetingApi.deleteMeeting(groupId, meetingId)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(resp.message()))
        }
    }

    override suspend fun createMeeting(
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
    ): Result<Unit> {
        val createMeetingRequestDto = CreateMeetingRequestDto(
            type = type,
            title = title,
            startAt = startDateTime.toInstant(ZoneOffset.UTC).toString(),
            placeName = placeName,
            geoPoint = CreateMeetingRequestDto.GeoPoint(longitude, latitude),
            capacity = capacity,
            cost = cost
        )
        val requestBody = Json.encodeToString(createMeetingRequestDto)
            .toRequestBody("application/json".toMediaTypeOrNull())

        val imageFile = imagePath?.let { File(it) }
        val imageFilePart = imageFile?.let {
            MultipartBody.Part.createFormData(
                "image",
                it.name,
                it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        val resp = withContext(ioDispatcher) {
            meetingApi.createMeeting(groupId, requestBody, imageFilePart)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(resp.message()))
        }
    }

    override suspend fun joinMeeting(
        groupId: Int,
        meetingId: Int
    ): Result<JoinMeetingResult> {
        val resp = withContext(ioDispatcher) {
            meetingApi.joinMeeting(groupId, meetingId)
        }

        return when {
            resp.isSuccessful -> {
                Result.success(JoinMeetingResult.SUCCESS)
            }

            resp.code() == 404 -> {
                Result.success(JoinMeetingResult.NOT_FOUND)
            }

            resp.code() == 409 -> {
                Result.success(JoinMeetingResult.OVER_CAPACITY)
            }

            else -> {
                Result.failure(Exception(resp.message()))
            }
        }
    }

    override suspend fun leaveMeeting(
        groupId: Int,
        meetingId: Int
    ): Result<LeaveMeetingResult> {
        val resp = withContext(ioDispatcher) {
            meetingApi.leaveMeeting(groupId, meetingId)
        }

        return when {
            resp.isSuccessful -> {
                Result.success(LeaveMeetingResult.SUCCESS)
            }

            resp.code() == 404 -> {
                Result.success(LeaveMeetingResult.NOT_FOUND)
            }

            else -> {
                Result.failure(Exception(resp.message()))
            }
        }
    }

    override fun getUpcomingMeetingsByDate(date: LocalDate): Flow<List<Meeting>> = flow {
        val resp = meetingApi.getUpcomingMeetings(
            date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(date)
        )
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val meetings = data.content.map {
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
            }
            emit(meetings)
        } else {
            throw Exception(resp.message())
        }
    }.flowOn(ioDispatcher)

    override fun getUpcomingMeetingPagingData(
        filters: Set<UpcomingMeetingsFilter>,
        groupId: Int?,
        size: Int
    ): Flow<PagingData<Meeting>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ComingSchedulePagingSource(meetingApi, filters, groupId) }
        ).flow.flowOn(ioDispatcher)
    }
}