package com.onmoim.core.network.di

import com.onmoim.core.network.api.AuthApi
import com.onmoim.core.network.api.LocationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideAuthApi(retrofit: Retrofit) = retrofit.create(AuthApi::class.java)

    @Provides
    fun provideLocationApi(retrofit: Retrofit) = retrofit.create(LocationApi::class.java)
}