package com.onmoim.core.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HttpClientType(val type: OnmoimHttpClientType)

enum class OnmoimHttpClientType {
    DEFAULT, AUTH
}
