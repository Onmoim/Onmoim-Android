package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.user.ProfileDto
import com.onmoim.core.network.model.user.RecentGroupDto
import com.onmoim.core.network.model.user.SetCategoryRequest
import com.onmoim.core.network.model.user.SignUpDto
import com.onmoim.core.network.model.user.SignUpRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("api/v1/user/signup")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<BaseResponse<SignUpDto>>

    @POST("api/v1/user/category")
    suspend fun setCategory(
        @Body setCategoryRequest: SetCategoryRequest
    ): Response<BaseResponse<String>>

    @GET("api/v1/user/profile")
    suspend fun getMyProfile(): Response<BaseResponse<ProfileDto>>

    @DELETE("api/v1/user/{id}")
    suspend fun withdrawal(
        @Path("id") id: Int
    ): Response<BaseResponse<String>>

    @GET("api/v1/user/recent/groups")
    suspend fun getRecentGroups(
        @Query("cursorViewedAt") cursorViewedAt: String? = null,
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<RecentGroupDto>>

    @Multipart
    @PUT("api/v1/user/profile/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int,
        @Part("request") requestBody: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Response<BaseResponse<String>>
}