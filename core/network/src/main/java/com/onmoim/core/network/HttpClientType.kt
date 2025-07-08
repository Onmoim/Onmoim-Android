package com.onmoim.core.network

import javax.inject.Qualifier

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION
)
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpClientType(val type: OnmoimHttpClientType)

enum class OnmoimHttpClientType {
    DEFAULT, AUTH
}
