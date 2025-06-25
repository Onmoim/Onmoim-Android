package com.onmoim.core.data.di

import com.onmoim.core.data.repository.AuthRepository
import com.onmoim.core.data.repository.AuthRepositoryImpl
import com.onmoim.core.data.repository.LocationRepository
import com.onmoim.core.data.repository.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository
}