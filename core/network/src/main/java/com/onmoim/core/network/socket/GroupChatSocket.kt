package com.onmoim.core.network.socket

import android.util.Log
import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.datastore.PreferencesKey
import com.onmoim.core.network.BuildConfig
import com.onmoim.core.network.model.chat.ChatSendTextDto
import com.onmoim.core.network.model.chat.SubscribeResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
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
    companion object {
        private val TAG = GroupChatSocket::class.java.simpleName
    }

    private lateinit var session: StompSessionWithKxSerialization

    suspend fun connect() {
        val accessToken = dataStorePreferences.getString(PreferencesKey.ACCESS_TOKEN).first()
            ?: throw Exception("accessToken is null")
        val headers = mapOf("Authorization" to accessToken)
        session = stompClient.connect(
            url = "${BuildConfig.BASE_URL_WS}/ws-chat",
            customStompConnectHeaders = headers
        ).withJsonConversions()
        subscribeSystemTopic()
    }

    suspend fun disconnect() {
        session.disconnect()
    }

    private suspend fun subscribeSystemTopic() {
        subscribe("/system/queue").catch {
            Log.e(TAG, "시스템 토픽 에러", it)
        }.collect {
            Log.d(TAG, "[SystemTopic] [${it.type}] ${it.content}")
        }
    }

    suspend fun subscribe(destination: String) =
        session.subscribe(destination, SubscribeResponse.serializer())

    suspend fun send(chatSendTextDto: ChatSendTextDto) {
        session.convertAndSend("/app/chat.sendMessage", chatSendTextDto, ChatSendTextDto.serializer())
    }
}