package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.post.BasePostPageDto
import com.onmoim.core.network.model.post.PostDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {
    @GET("api/v1/groups/{groupId}/posts")
    suspend fun getPosts(
        @Path("groupId") groupId: Int,
        @Query("type") type: String? = null,
        @Query("cursorId") cursorId: Int? = null,
        @Query("size") size: Int? = null,
    ): Response<BaseResponse<BasePostPageDto<PostDto>>>

    @Multipart
    @POST("api/v1/groups/{groupId}/posts")
    suspend fun createPost(
        @Path("groupId") groupId: Int,
        @Part("request") requestBody: RequestBody,
        @Part files: List<MultipartBody.Part>? = null,
    ): Response<BaseResponse<PostDto>>
}