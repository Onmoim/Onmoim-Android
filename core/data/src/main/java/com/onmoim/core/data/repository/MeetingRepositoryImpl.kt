package com.onmoim.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onmoim.core.data.constant.MeetingType
import com.onmoim.core.data.model.Meeting
import com.onmoim.core.data.pagingsource.MeetingPagingSource
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.MeetingApi
import com.onmoim.core.network.model.meeting.CreateMeetingRequestDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
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
}