package com.onmoim.core.domain.di

import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.AuthRepository
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.core.domain.usecase.GetUserIdUseCase
import com.onmoim.core.domain.usecase.SignInUseCase
import com.onmoim.core.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideSignInUseCase(
        authRepository: AuthRepository,
        tokenRepository: TokenRepository,
        appSettingRepository: AppSettingRepository
    ) = SignInUseCase(authRepository, tokenRepository, appSettingRepository)

    @Provides
    fun provideSignUpUseCase(
        userRepository: UserRepository,
        tokenRepository: TokenRepository,
        appSettingRepository: AppSettingRepository
    ) = SignUpUseCase(userRepository, tokenRepository, appSettingRepository)

    @Provides
    fun provideGetUserIdUseCase(
        appSettingRepository: AppSettingRepository,
        userRepository: UserRepository
    ) = GetUserIdUseCase(appSettingRepository, userRepository)
}