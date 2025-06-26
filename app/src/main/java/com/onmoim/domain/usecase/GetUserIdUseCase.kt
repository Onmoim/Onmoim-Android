package com.onmoim.domain.usecase

import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val appSettingRepository: AppSettingRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Int {
        val cachedUserId = appSettingRepository.getUserId()

        if (cachedUserId == null) {
            val userId = userRepository.getMyProfile().first().id
            appSettingRepository.setUserId(userId)
            return userId
        } else {
            return cachedUserId
        }
    }
}