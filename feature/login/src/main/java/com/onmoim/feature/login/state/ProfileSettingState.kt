package com.onmoim.feature.login.state

import com.onmoim.core.designsystem.constant.Gender
import java.time.LocalDate

data class ProfileSettingState(
    val name: String = "",
    val gender: Gender? = null,
    val birth: LocalDate? = null,
    val location: String = "",
    val locationId: Int = 0,
) {
    val isValidInputValue: Boolean
        get() = isValidName() && gender != null && birth != null && location.isNotBlank()

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
