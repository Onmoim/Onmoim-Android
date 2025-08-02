package com.onmoim.core.network.di

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.network.socket.GroupChatSocket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Provides
    fun providesStompClient(
        okHttpWebSocketClient: OkHttpWebSocketClient
    ) = StompClient(okHttpWebSocketClient)

    @Provides
    @Singleton
    fun providesGroupChatSocket(
        stompClient: StompClient,
        dataStorePreferences: DataStorePreferences
    ) = GroupChatSocket(stompClient, dataStorePreferences)
}