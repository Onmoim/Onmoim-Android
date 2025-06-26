package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.CategoryDto
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET("api/v1/category")
    suspend fun getCategories(): Response<BaseResponse<List<CategoryDto>>>
}