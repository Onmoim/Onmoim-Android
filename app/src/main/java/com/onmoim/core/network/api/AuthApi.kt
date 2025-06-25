package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.auth.SignInRequest
import com.onmoim.core.network.model.auth.TokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/v1/auth/oauth")
    suspend fun signIn(
        @Body signInRequest: SignInRequest
    ): Response<BaseResponse<TokenDto>>
}