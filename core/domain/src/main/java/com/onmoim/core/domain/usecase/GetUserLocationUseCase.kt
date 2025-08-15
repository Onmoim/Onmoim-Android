package com.onmoim.core.domain.usecase

import android.util.Log
import com.onmoim.core.data.model.Profile
import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val appSettingRepository: AppSettingRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Pair<Int, String>?> = appSettingRepository.getUserLocationFlow().map { locationPair ->
        locationPair ?: userRepository.getMyProfile().catch<Profile?> {
            Log.e("GetUserLocationUseCase", "error: ${it.message}", it)
            emit(null)
        }.firstOrNull()?.let { profile ->
            Pair(profile.locationId, profile.location)
        }?.also {
            appSettingRepository.setUserLocation(it.first, it.second)
        }
    }
}