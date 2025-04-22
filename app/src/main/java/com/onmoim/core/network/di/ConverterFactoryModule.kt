package com.onmoim.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConverterFactoryModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class KotlinxSerializationConverterFactory

    @Provides
    @Singleton
    @KotlinxSerializationConverterFactory
    fun provideKotlinxSerializationConverterFactory() =
        Json.asConverterFactory("application/json; charset=UTF-8".toMediaType())
}