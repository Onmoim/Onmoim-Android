package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.user.SetCategoryRequest
import com.onmoim.core.network.model.user.SignUpDto
import com.onmoim.core.network.model.user.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("api/v1/user/signup")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<BaseResponse<SignUpDto>>

    @POST("api/v1/user/category")
    suspend fun setCategory(
        @Body setCategoryRequest: SetCategoryRequest
    ): Response<BaseResponse<String>>
}