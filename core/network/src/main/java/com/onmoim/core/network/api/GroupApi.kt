package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.group.CreateGroupRequest
import com.onmoim.core.network.model.group.CreatedGroupDto
import com.onmoim.core.network.model.group.GroupDetailDto
import com.onmoim.core.network.model.group.GroupStatisticsDto
import com.onmoim.core.network.model.group.PopularGroupDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {
    @GET("api/v1/groups/nearby/popular")
    suspend fun getPopularNearbyGroups(
        @Query("lastGroupIdx") lastGroupIdx: Int? = null,
        @Query("requestSize") requestSize: Int = 10,
        @Query("memberCount") memberCount: Int? = null
    ): Response<BaseResponse<PopularGroupDto>>

    @GET("api/v1/groups/active/popular")
    suspend fun getPopularActiveGroups(
        @Query("lastGroupIdx") lastGroupIdx: Int? = null,
        @Query("requestSize") requestSize: Int = 10,
        @Query("memberCount") memberCount: Int? = null
    ): Response<BaseResponse<PopularGroupDto>>

    @POST("api/v1/groups")
    suspend fun createGroup(
        @Body createGroupRequest: CreateGroupRequest
    ): Response<BaseResponse<CreatedGroupDto>>

    @GET("api/v1/groups/{groupId}")
    suspend fun getGroupDetail(
        @Path("groupId") groupId: Int
    ): Response<BaseResponse<GroupDetailDto>>

    @DELETE("api/v1/groups/{groupId}/member")
    suspend fun leaveGroup(
        @Path("groupId") groupId: Int
    ): Response<BaseResponse<String>>

    @DELETE("api/v1/groups/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId: Int
    ): Response<BaseResponse<String>>

    @POST("api/v1/groups/{groupId}/like")
    suspend fun likeGroup(
        @Path("groupId") groupId: Int
    ): Response<BaseResponse<String>>

    @GET("api/v1/groups/{groupId}/statistics")
    suspend fun getGroupStatistics(
        @Path("groupId") groupId: Int
    ): Response<BaseResponse<GroupStatisticsDto>>
}