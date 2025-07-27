package com.onmoim.core.network

import javax.inject.Qualifier

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION
)
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiType(val type: OnmoimApiType)

enum class OnmoimApiType {
    DEFAULT, AUTH, KAKAO
}
