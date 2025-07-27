package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.LikedGroupDto
import com.onmoim.core.network.model.MemberDto
import com.onmoim.core.network.model.MemberIdRequestDto
import com.onmoim.core.network.model.RecommendGroupDto
import com.onmoim.core.network.model.group.BaseGroupPageDto
import com.onmoim.core.network.model.group.CreateGroupRequest
import com.onmoim.core.network.model.group.CreatedGroupDto
import com.onmoim.core.network.model.group.GroupDetailDto
import com.onmoim.core.network.model.group.GroupStatisticsDto
import com.onmoim.core.network.model.group.PopularGroupDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupApi {
    @GET("api/v1/groups/nearby/popular")
    suspend fun getPopularNearbyGroups(
        @Query("lastGroupIdx") lastGroupIdx: Int? = null,
        @Query("requestSize") requestSize: Int? = null,
        @Query("memberCount") memberCount: Int? = null
    ): Response<BaseResponse<PopularGroupDto>>

    @GET("api/v1/groups/active/popular")
    suspend fun getPopularActiveGroups(
        @Query("lastGroupIdx") lastGroupIdx: Int? = null,
        @Query("requestSize") requestSize: Int? = null,
        @Query("memberCount") memberCount: Int? = null
    ): Response<BaseResponse<PopularGroupDto>>

    @GET("api/v1/groups/recommend/category")
    suspend fun getRecommendCategoryGroups(
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<RecommendGroupDto>>

    @GET("api/v1/groups/recommend/location")
    suspend fun getRecommendLocationGroups(
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<RecommendGroupDto>>

    @GET("api/v1/groups/liked")
    suspend fun getLikedGroups(
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<LikedGroupDto>>

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

    @GET("api/v1/groups/{groupId}/members")
    suspend fun getGroupMembers(
        @Path("groupId") groupId: Int,
        @Query("lastMemberId") lastMemberId: Int? = null,
        @Query("requestSize") requestSize: Int? = null
    ): Response<BaseResponse<BaseGroupPageDto<MemberDto>>>

    @POST("api/v1/groups/{groupId}/ban")
    suspend fun banMember(
        @Path("groupId") groupId: Int,
        @Body memberIdRequestDto: MemberIdRequestDto
    ): Response<BaseResponse<String>>

    @PATCH("api/v1/groups/{groupId}/owner")
    suspend fun transferGroupOwner(
        @Path("groupId") groupId: Int,
        @Body memberIdRequestDto: MemberIdRequestDto
    ): Response<BaseResponse<String>>

    @Multipart
    @PATCH("api/v1/groups/{groupId}")
    suspend fun updateGroup(
        @Path("groupId") groupId: Int,
        @Part("request") requestBody: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): Response<BaseResponse<String>>

    @POST("api/v1/groups/{groupId}/join")
    suspend fun joinGroup(
        @Path("groupId") groupId: Int
    ): Response<BaseResponse<String>>
}