package com.onmoim.core.data.repository

import com.onmoim.core.data.constant.SocketConnectionEvent
import com.onmoim.core.data.model.Message
import com.onmoim.core.data.model.SystemMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun connect()
    suspend fun disconnect()
    suspend fun sendMessage(groupId: Int, message: String)
    suspend fun receiveMessages(groupId: Int): Flow<Message>
    suspend fun receiveSystemMessages(): Flow<SystemMessage>
    suspend fun receiveConnectionEvent(): Flow<SocketConnectionEvent>
}