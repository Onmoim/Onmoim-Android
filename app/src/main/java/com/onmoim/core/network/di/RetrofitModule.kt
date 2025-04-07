package com.onmoim.core.network.di

import com.onmoim.BuildConfig
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

    @Provides
    @Singleton
    fun provideRetrofit(
        @KotlinxSerializationConverterFactory serializationConverterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder().apply {
        baseUrl(BuildConfig.BASE_URL)
        addConverterFactory(serializationConverterFactory)
        client(okHttpClient)
    }.build()
}