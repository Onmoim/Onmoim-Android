package com.onmoim.feature.login.state

import com.onmoim.core.constant.Gender
import timber.log.Timber
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
            Timber.d("이름은 비어있거나 공백만 있을 수 없음.")
            return false
        }

        if (name.length < minLength || name.length > maxLength) {
            Timber.d("이름 길이는 ${minLength}자 이상 ${maxLength}자 이하여야 함. (입력값: '${name}', 길이: ${name.length})")
            return false
        }

        if (!isValidKoreanNameFormat()) {
            Timber.d("이름은 한글('가'~'힣')로만 구성되어야 함. (입력값: '${name}')")
            val invalidChars = name.filterNot { it in '가'..'힣' }
            if (invalidChars.isNotEmpty()) {
                Timber.d("   >> 포함된 잘못된 문자: '${invalidChars}'")
            }
            return false
        }

        return true
    }

    fun isValidKoreanNameFormat(): Boolean {
        val koreanNameRegex = Regex("^[가-힣]+$")
        return koreanNameRegex.matches(name)
    }
}
