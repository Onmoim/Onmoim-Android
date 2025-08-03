package com.onmoim.core.data.repository

import com.onmoim.core.data.constant.SocketConnectionEvent
import com.onmoim.core.data.constant.SystemMessageType
import com.onmoim.core.data.model.Message
import com.onmoim.core.data.model.SystemMessage
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.model.chat.ChatSendTextDto
import com.onmoim.core.network.socket.GroupChatConnectionEvent
import com.onmoim.core.network.socket.GroupChatSocket
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val groupChatSocket: GroupChatSocket,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : ChatRepository {
    override suspend fun connect() {
        withContext(ioDispatcher) {
            groupChatSocket.connect()
        }
    }

    override suspend fun disconnect() {
        withContext(ioDispatcher) {
            groupChatSocket.disconnect()
        }
    }

    override suspend fun sendMessage(
        groupId: Int,
        message: String
    ) {
        val chatSendTextDto = ChatSendTextDto(
            content = message,
            groupId = groupId,
            type = "CHAT"
        )

        withContext(ioDispatcher) {
            groupChatSocket.sendMessage(chatSendTextDto)
        }
    }

    override suspend fun receiveMessages(groupId: Int): Flow<Message> =
        groupChatSocket.subscribeChatRoomTopic(groupId).map {
            val systemZoneId = ZoneId.systemDefault()
            val timestamp =
                "${it.timestamp[0]}-${it.timestamp[1]}-${it.timestamp[2]}T${it.timestamp[3]}:${it.timestamp[4]}:${it.timestamp[5]}.${it.timestamp[6]}Z"
            val sendLocalDateTimeUTC = LocalDateTime.parse(timestamp)
            val sendZonedDateTime = sendLocalDateTimeUTC.atZone(systemZoneId)

            Message(
                messageSequence = it.messageSequence,
                senderId = it.chatUserDto.id,
                userName = it.chatUserDto.username,
                profileImageUrl = it.chatUserDto.profileImageUrl,
                content = it.content,
                sendDateTime = sendZonedDateTime.toLocalDateTime(),
                isOwner = it.chatUserDto.owner
            )
        }.flowOn(ioDispatcher)

    override suspend fun receiveSystemMessages(): Flow<SystemMessage> =
        groupChatSocket.subscribeSystemTopic().map {
            SystemMessage(
                type = when (it.type) {
                    "SUCCESS" -> SystemMessageType.SUCCESS
                    "ERROR" -> SystemMessageType.ERROR
                    else -> SystemMessageType.NONE
                },
                message = it.content
            )
        }.flowOn(ioDispatcher)

    override suspend fun receiveConnectionEvent(): Flow<SocketConnectionEvent> =
        groupChatSocket.event.map {
            when (it) {
                GroupChatConnectionEvent.Connected -> SocketConnectionEvent.Connected
                GroupChatConnectionEvent.Connecting -> SocketConnectionEvent.Connecting
                GroupChatConnectionEvent.Disconnected -> SocketConnectionEvent.Disconnected
                GroupChatConnectionEvent.Disconnecting -> SocketConnectionEvent.Disconnecting
                is GroupChatConnectionEvent.Error -> SocketConnectionEvent.Error(it.t)
                GroupChatConnectionEvent.NotAuthenticated -> SocketConnectionEvent.NotAuthenticated
            }
        }
}