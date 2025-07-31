package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.meeting.BaseMeetingPageDto
import com.onmoim.core.network.model.meeting.MeetingDto
import com.onmoim.core.network.model.meeting.UpcomingMeetingsDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @DELETE("api/v1/groups/{groupId}/meetings/{meetingId}")
    suspend fun deleteMeeting(
        @Path("groupId") groupId: Int,
        @Path("meetingId") meetingId: Int
    ): Response<BaseResponse<String>>

    @Multipart
    @POST("api/v1/groups/{groupId}/meetings")
    suspend fun createMeeting(
        @Path("groupId") groupId: Int,
        @Part("request") requestBody: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Response<BaseResponse<Int>>

    @POST("api/v1/groups/{groupId}/meetings/{meetingId}/join")
    suspend fun joinMeeting(
        @Path("groupId") groupId: Int,
        @Path("meetingId") meetingId: Int
    ): Response<BaseResponse<String>>

    @POST("api/v1/groups/{groupId}/meetings/{meetingId}/leave")
    suspend fun leaveMeeting(
        @Path("groupId") groupId: Int,
        @Path("meetingId") meetingId: Int
    ): Response<BaseResponse<String>>

    @GET("api/v1/meetings/upcoming")
    suspend fun getUpcomingMeetings(
        @Query("date") date: String? = null,
        @Query("thisWeekYn") thisWeekYn: Boolean? = null,
        @Query("thisMonthYn") thisMonthYn: Boolean? = null,
        @Query("joinedYn") joinedYn: Boolean? = null,
        @Query("regularYn") regularYn: Boolean? = null,
        @Query("flashYn") flashYn: Boolean? = null,
        @Query("cursorStartAt") cursorStartAt: String? = null,
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<UpcomingMeetingsDto>>
}