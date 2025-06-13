package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.LocationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("/api/v1/location")
    suspend fun searchLocation(
        @Query("dong") query: String
    ): Response<BaseResponse<List<LocationDto>>>
}