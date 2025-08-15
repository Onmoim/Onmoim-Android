package com.onmoim.core.network.api

import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.chat.ChatMessageDto
import com.onmoim.core.network.model.chat.ChatRoomDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("/api/chat/rooms")
    suspend fun getChatRooms(
        @Query("cursorTime") cursorTime: String? = null,
        @Query("cursorGroupName") cursorGroupName: String? = null,
        @Query("size") size: Int? = null
    ): Response<BaseResponse<List<ChatRoomDto>>>

    @GET("/api/chat/rooms/{roomId}/messages")
    suspend fun getChatMessages(
        @Path("roomId") roomId: Int,
        @Query("cursor") cursor: Int? = null
    ): Response<BaseResponse<List<ChatMessageDto>>>
}