package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.model.ChatRoom
import com.onmoim.core.network.api.ChatApi
import java.time.LocalDateTime
import java.time.ZoneId

class ChatRoomPagingSource(
    private val chatApi: ChatApi
) : PagingSource<String, ChatRoom>() {
    override fun getRefreshKey(state: PagingState<String, ChatRoom>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ChatRoom> {
        return try {
            val key = params.key
            val resp = chatApi.getChatRooms(
                cursorTime = key,
                size = params.loadSize
            )
            val data = resp.body()?.data
            val chatRooms = data?.map {
                val messageLocalDateTime = LocalDateTime.parse(it.message.timestamp)
                val messageDateTimeUTC = messageLocalDateTime.atZone(ZoneId.of("UTC"))
                val messageZonedDateTime =
                    messageDateTimeUTC.withZoneSameInstant(ZoneId.systemDefault())

                ChatRoom(
                    groupId = it.groupId,
                    title = it.name,
                    roomMemberCount = it.memberCount,
                    lastSentDateTime = messageZonedDateTime.toLocalDateTime(),
                    lastSentMessage = it.message.content,
                    imageUrl = it.imgUrl
                )
            } ?: emptyList()
            val nextKey = if (chatRooms.size < params.loadSize) null else data?.lastOrNull()?.message?.timestamp
            LoadResult.Page(data = chatRooms, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("ChatRoomPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}