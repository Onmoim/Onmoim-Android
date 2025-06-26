package com.onmoim.core.data.di

import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.AppSettingRepositoryImpl
import com.onmoim.core.data.repository.AuthRepository
import com.onmoim.core.data.repository.AuthRepositoryImpl
import com.onmoim.core.data.repository.InterestRepository
import com.onmoim.core.data.repository.InterestRepositoryImpl
import com.onmoim.core.data.repository.LocationRepository
import com.onmoim.core.data.repository.LocationRepositoryImpl
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.data.repository.TokenRepositoryImpl
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.core.data.repository.UserRepositoryImpl
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

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    abstract fun bindInterestRepository(
        interestRepositoryImpl: InterestRepositoryImpl
    ): InterestRepository

    @Binds
    abstract fun bindAppSettingRepository(
        appSettingRepositoryImpl: AppSettingRepositoryImpl
    ): AppSettingRepository
}