package com.onmoim.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient

@Module
@InstallIn(SingletonComponent::class)
object StompModule {

    @Provides
    fun providesStompClient(
        okHttpWebSocketClient: OkHttpWebSocketClient
    ) = StompClient(okHttpWebSocketClient)
}