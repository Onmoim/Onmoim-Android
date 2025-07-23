package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.meeting.BaseMeetingPageDto
import com.onmoim.core.network.model.meeting.MeetingDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MeetingApi {
    @GET("api/v1/groups/{groupId}/meetings")
    suspend fun getMeetings(
        @Path("groupId") groupId: Int,
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null,
        @Query("type") type: String? = null // REGULAR: 정기모임, FLASH: 번개모임
    ): Response<BaseResponse<BaseMeetingPageDto<MeetingDto>>>
}