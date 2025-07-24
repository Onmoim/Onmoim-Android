package com.onmoim.core.network.di

import com.onmoim.core.network.ApiType
import com.onmoim.core.network.BuildConfig
import com.onmoim.core.network.OnmoimApiType
import com.onmoim.core.network.di.ConverterFactoryModule.KotlinxSerializationConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @ApiType(OnmoimApiType.AUTH)
    @Provides
    @Singleton
    fun provideAuthRetrofit(
        @KotlinxSerializationConverterFactory serializationConverterFactory: Converter.Factory,
        @ApiType(OnmoimApiType.AUTH) okHttpClient: OkHttpClient
    ) = Retrofit.Builder().apply {
        baseUrl(BuildConfig.BASE_URL)
        addConverterFactory(serializationConverterFactory)
        client(okHttpClient)
    }.build()

    @ApiType(OnmoimApiType.KAKAO)
    @Provides
    @Singleton
    fun provideKakaoRetrofit(
        @KotlinxSerializationConverterFactory serializationConverterFactory: Converter.Factory,
        @ApiType(OnmoimApiType.KAKAO) okHttpClient: OkHttpClient
    ) = Retrofit.Builder().apply {
        baseUrl("https://dapi.kakao.com")
        addConverterFactory(serializationConverterFactory)
        client(okHttpClient)
    }.build()
}