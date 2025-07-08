package com.onmoim.core.domain.usecase

import com.onmoim.core.data.constant.AccountStatus
import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.data.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val appSettingRepository: AppSettingRepository
) {
    suspend operator fun invoke(
        locationId: Int,
        birth: String,
        gender: String,
        name: String
    ): Result<AccountStatus> {
        return try {
            val account = userRepository.signUp(locationId, birth, gender, name)
            val jwt = account.jwt
            tokenRepository.setJwt(jwt.accessToken, jwt.refreshToken)
            account.userId?.let {
                appSettingRepository.setUserId(it)
            }
            appSettingRepository.setHasNotInterest(account.status == AccountStatus.NO_CATEGORY)
            Result.success(account.status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}