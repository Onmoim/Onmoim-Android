package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.post.BasePostPageDto
import com.onmoim.core.network.model.post.PostDto
import retrofit2.Response
import retrofit2.http.GET
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
}