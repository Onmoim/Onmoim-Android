package com.onmoim.core.network.di

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.event.AuthEventBus
import com.onmoim.core.network.ApiType
import com.onmoim.core.network.BuildConfig
import com.onmoim.core.network.OnmoimApiType
import com.onmoim.core.network.OnmoimAuthInterceptor
import com.onmoim.core.network.OnmoimAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @ApiType(OnmoimApiType.DEFAULT)
    @Provides
    @Singleton
    fun provideDefaultOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder().apply {
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @ApiType(OnmoimApiType.AUTH)
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
        callTimeout(Duration.ofMinutes(1))
        pingInterval(Duration.ofSeconds(10))
    }.build()

    @ApiType(OnmoimApiType.KAKAO)
    @Provides
    @Singleton
    fun provideKakaoOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain ->
            val request = chain.request()
                .newBuilder().apply {
                    addHeader("Authorization", "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}")
                }.build()
            chain.proceed(request)
        })
        addInterceptor(httpLoggingInterceptor)
    }.build()

    @Provides
    @Singleton
    fun provideOnmoimAuthenticator(
        dataStorePreferences: DataStorePreferences,
        authEventBus: AuthEventBus,
        @ApiType(OnmoimApiType.DEFAULT) okHttpClient: OkHttpClient,
        @Dispatcher(OnmoimDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): Authenticator = OnmoimAuthenticator(
        dataStorePreferences = dataStorePreferences,
        authEventBus = authEventBus,
        client = okHttpClient,
        ioDispatcher = ioDispatcher
    )

    @Provides
    @Singleton
    fun provideOnmoimAuthInterceptor(
        dataStorePreferences: DataStorePreferences,
        @Dispatcher(OnmoimDispatcher.IO) ioDispatcher: CoroutineDispatcher
    ): Interceptor = OnmoimAuthInterceptor(dataStorePreferences, ioDispatcher)

    @Provides
    @Singleton
    fun provideOkHttpWebSocketClient(
        @ApiType(OnmoimApiType.AUTH) okHttpClient: OkHttpClient
    ) = OkHttpWebSocketClient(okHttpClient)
}