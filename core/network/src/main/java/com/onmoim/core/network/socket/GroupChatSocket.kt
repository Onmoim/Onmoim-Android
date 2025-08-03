package com.onmoim.core.network.socket

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.datastore.PreferencesKey
import com.onmoim.core.network.BuildConfig
import com.onmoim.core.network.model.chat.ChatRoomTopicDto
import com.onmoim.core.network.model.chat.ChatSendTextDto
import com.onmoim.core.network.model.chat.SystemTopicDto
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import javax.inject.Inject

class GroupChatSocket @Inject constructor(
    private val stompClient: StompClient,
    private val dataStorePreferences: DataStorePreferences
) {
    private lateinit var session: StompSessionWithKxSerialization
    private val _event = Channel<GroupChatConnectionEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    suspend fun connect() {
        try {
            _event.send(GroupChatConnectionEvent.Connecting)

            val accessToken = dataStorePreferences.getString(PreferencesKey.ACCESS_TOKEN).first()

            if (accessToken == null) {
                _event.send(GroupChatConnectionEvent.NotAuthenticated)
                return
            }

            val headers = mapOf("Authorization" to accessToken)
            session = stompClient.connect(
                url = "${BuildConfig.BASE_URL_WS}/ws-chat",
                customStompConnectHeaders = headers
            ).withJsonConversions()

            _event.send(GroupChatConnectionEvent.Connected)
        } catch (e: Exception) {
            _event.send(GroupChatConnectionEvent.Error(e))
        }
    }

    suspend fun disconnect() {
        _event.send(GroupChatConnectionEvent.Disconnecting)
        session.disconnect()
        _event.send(GroupChatConnectionEvent.Disconnected)
    }

    suspend fun subscribeSystemTopic() =
        session.subscribe("/system/queue", SystemTopicDto.serializer())

    suspend fun subscribeChatRoomTopic(groupId: Int) =
        session.subscribe("/topic/chat.room.${groupId}", ChatRoomTopicDto.serializer())

    suspend fun sendMessage(chatSendTextDto: ChatSendTextDto) {
        session.convertAndSend(
            "/app/chat.sendMessage",
            chatSendTextDto,
            ChatSendTextDto.serializer()
        )
    }
}