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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
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
}