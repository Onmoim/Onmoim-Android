package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.group.PopularActiveGroupDto
import com.onmoim.core.network.model.group.PopularGroupDto
import retrofit2.Response
import retrofit2.http.GET
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
}