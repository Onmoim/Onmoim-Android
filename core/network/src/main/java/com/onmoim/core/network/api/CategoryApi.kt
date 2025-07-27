package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.category.CategoryDto
import com.onmoim.core.network.model.category.CategoryGroupDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApi {
    @GET("api/v1/category")
    suspend fun getCategories(): Response<BaseResponse<List<CategoryDto>>>

    @GET("api/v1/category/{categoryId}/groups")
    suspend fun getGroupsByCategory(
        @Path("categoryId") categoryId: Int,
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<CategoryGroupDto>>
}