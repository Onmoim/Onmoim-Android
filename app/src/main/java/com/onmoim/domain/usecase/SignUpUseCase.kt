package com.onmoim.domain.usecase

import com.onmoim.core.constant.AccountStatus
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.data.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(
        addressId: Int,
        birth: String,
        gender: String,
        name: String
    ): Result<AccountStatus> {
        return try {
            val account = userRepository.signUp(addressId, birth, gender, name)
            val jwt = account.jwt
            tokenRepository.setJwt(jwt.accessToken, jwt.refreshToken)
            Result.success(account.status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}