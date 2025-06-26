package com.onmoim.domain.usecase

import com.onmoim.core.constant.AccountStatus
import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.AuthRepository
import com.onmoim.core.data.repository.TokenRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val appSettingRepository: AppSettingRepository
) {
    operator fun invoke(provider: String, token: String) =
        authRepository.signIn(provider, token).onEach {
            val jwt = it.jwt
            tokenRepository.setJwt(jwt.accessToken, jwt.refreshToken)
            appSettingRepository.setHasNotInterest(it.status == AccountStatus.NO_CATEGORY)
        }.map {
            it.status
        }
}