package com.onmoim.core.network.di

import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.HttpClientType
import com.onmoim.core.network.OnmoimAuthInterceptor
import com.onmoim.core.network.OnmoimAuthenticator
import com.onmoim.core.network.OnmoimHttpClientType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @HttpClientType(OnmoimHttpClientType.DEFAULT)
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder().apply {
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @HttpClientType(OnmoimHttpClientType.AUTH)
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        onmoimAuthInterceptor: OnmoimAuthInterceptor,
        onmoimAuthenticator: OnmoimAuthenticator
    ) = OkHttpClient.Builder().apply {
        addInterceptor(httpLoggingInterceptor)
        addInterceptor(onmoimAuthInterceptor)
        authenticator(onmoimAuthenticator)
    }.build()

    @Provides
    @Singleton
    fun provideOnmoimAuthenticator(
        tokenRepository: TokenRepository,
        @HttpClientType(OnmoimHttpClientType.DEFAULT) okHttpClient: OkHttpClient,
        @Dispatcher(OnmoimDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): Authenticator = OnmoimAuthenticator(tokenRepository, okHttpClient, ioDispatcher)

    @Provides
    @Singleton
    fun provideOnmoimAuthInterceptor(
        tokenRepository: TokenRepository,
        @Dispatcher(OnmoimDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): Interceptor = OnmoimAuthInterceptor(tokenRepository, ioDispatcher)
}