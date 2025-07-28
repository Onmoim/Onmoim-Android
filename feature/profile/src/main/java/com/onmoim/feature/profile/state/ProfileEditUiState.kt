package com.onmoim.feature.profile.state

import com.onmoim.core.designsystem.constant.Gender
import java.time.LocalDate

data class ProfileEditUiState(
    val id: Int? = null,
    val name: String = "",
    val gender: Gender? = null,
    val birth: LocalDate? = null,
    val locationId: Int? = null,
    val locationName: String = "",
    val introduction: String = "",
    val categoryIds: List<Int> = emptyList(),
    val categoryNames: List<String> = emptyList(),
    val originImageUrl: String? = null,
    val newImagePath: String? = null
) {
    val isValid: Boolean
        get() = isValidName() && gender != null && birth != null && locationId != null && locationName.isNotBlank() && categoryIds.isNotEmpty()

    private fun isValidName(minLength: Int = 2, maxLength: Int = 12): Boolean {
        if (name.isBlank()) {
            return false
        }

        if (name.length < minLength || name.length > maxLength) {
            return false
        }

        if (!isValidKoreanNameFormat()) {
            return false
        }

        return true
    }

    fun isValidKoreanNameFormat(): Boolean {
        val koreanNameRegex = Regex("^[가-힣]+$")
        return koreanNameRegex.matches(name)
    }
}