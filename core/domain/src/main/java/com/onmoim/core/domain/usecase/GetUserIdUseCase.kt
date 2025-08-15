package com.onmoim.core.domain.usecase

import android.util.Log
import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.retryWhen
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val appSettingRepository: AppSettingRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): Int {
        val cachedUserId = appSettingRepository.getUserId()

        if (cachedUserId == null) {
            val userId = userRepository.getMyProfile().retryWhen {
                cause, attempt ->
                delay(1000)
                attempt < 3
            }.catch {
                Log.e("GetUserIdUseCase", "getMyProfile error", it)
            }.firstOrNull()?.id ?: 0
            appSettingRepository.setUserId(userId)
            return userId
        } else {
            return cachedUserId
        }
    }
}