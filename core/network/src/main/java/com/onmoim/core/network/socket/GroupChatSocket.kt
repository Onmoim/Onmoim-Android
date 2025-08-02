package com.onmoim.core.network.socket

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.datastore.PreferencesKey
import com.onmoim.core.network.BuildConfig
import com.onmoim.core.network.model.chat.ChatSendTextDto
import com.onmoim.core.network.model.chat.SubscribeResponse
import kotlinx.coroutines.flow.first
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.convertAndSend
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.conversions.kxserialization.subscribe
import javax.inject.Inject

class GroupChatSocket @Inject constructor(
    private val stompClient: StompClient,
    private val dataStorePreferences: DataStorePreferences
) {
    private lateinit var session: StompSession
    private lateinit var jsonSession: StompSessionWithKxSerialization

    suspend fun connect() {
        val accessToken = dataStorePreferences.getString(PreferencesKey.ACCESS_TOKEN).first()
            ?: throw Exception("accessToken is null")
        val headers = mapOf("Authorization" to accessToken)
        session = stompClient.connect(
            url = BuildConfig.BASE_URL_WS,
            customStompConnectHeaders = headers
        )
        jsonSession = session.withJsonConversions()
    }

    suspend fun disconnect() {
        session.disconnect()
    }

    suspend fun subscribe(destination: String) =
        jsonSession.subscribe(destination, SubscribeResponse.serializer())

    suspend fun sendText(destination: String, chatSendTextDto: ChatSendTextDto) {
        jsonSession.convertAndSend(destination, chatSendTextDto, ChatSendTextDto.serializer())
    }
}