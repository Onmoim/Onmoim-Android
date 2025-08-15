package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.model.Message
import com.onmoim.core.network.api.ChatApi
import java.time.LocalDateTime
import java.time.ZoneId

class ChatMessagePagingSource(
    private val chatApi: ChatApi,
    private val roomId: Int
) : PagingSource<Int, Message>() {
    override fun getRefreshKey(state: PagingState<Int, Message>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Message> {
        return try {
            val key = params.key
            val resp = chatApi.getChatMessages(
                roomId = roomId,
                cursor = key
            )
            val data = resp.body()?.data
            val messages = data?.map {
                val messageLocalDateTime = LocalDateTime.parse(it.timestamp)
                val messageDateTimeUTC = messageLocalDateTime.atZone(ZoneId.of("UTC"))
                val messageZonedDateTime =
                    messageDateTimeUTC.withZoneSameInstant(ZoneId.systemDefault())

                Message(
                    messageSequence = it.messageSequence,
                    groupId = it.groupId,
                    senderId = it.senderId,
                    userName = it.chatUserDto.username,
                    profileImageUrl = it.chatUserDto.profileImageUrl,
                    content = it.content,
                    sendDateTime = messageZonedDateTime.toLocalDateTime(),
                    isOwner = it.chatUserDto.owner
                )
            } ?: emptyList()
            val nextKey = if (messages.size < 10) null else messages.lastOrNull()?.messageSequence
            LoadResult.Page(data = messages, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("ChatMessagePagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}