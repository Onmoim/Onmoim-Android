package com.onmoim.core.network.di

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
    fun provideLocationApi(retrofit: Retrofit) = retrofit.create(LocationApi::class.java)
}