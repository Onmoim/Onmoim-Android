package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.SocketConnectionState
import com.onmoim.core.data.model.ChatRoom
import com.onmoim.core.data.model.Message
import com.onmoim.core.data.model.SystemMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connect()
    suspend fun disconnect()
    suspend fun sendMessage(groupId: Int, message: String)
    suspend fun receiveMessages(groupId: Int): Flow<Message>
    suspend fun receiveSystemMessages(): Flow<SystemMessage>
    suspend fun receiveConnectionEvent(): Flow<SocketConnectionState>
    fun getChatRoomPagingData(size: Int = 20): Flow<PagingData<ChatRoom>>
    fun getChatRoomMessagePagingData(groupId: Int, size: Int = 20): Flow<PagingData<Message>>
}