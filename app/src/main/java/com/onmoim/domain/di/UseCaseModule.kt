package com.onmoim.domain.di

import com.onmoim.core.data.repository.AuthRepository
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.domain.usecase.SignInUseCase
import com.onmoim.domain.usecase.SignUpUseCase
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
        userRepository: UserRepository
    ) = SignInUseCase(authRepository, tokenRepository, userRepository)

    @Provides
    fun provideSignUpUseCase(
        userRepository: UserRepository,
        tokenRepository: TokenRepository
    ) = SignUpUseCase(userRepository, tokenRepository)
}