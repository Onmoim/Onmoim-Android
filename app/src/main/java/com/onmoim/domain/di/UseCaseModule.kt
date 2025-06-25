package com.onmoim.domain.di

import com.onmoim.core.data.repository.AuthRepository
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.domain.usecase.SignInUseCase
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
        tokenRepository: TokenRepository
    ) = SignInUseCase(authRepository, tokenRepository)
}