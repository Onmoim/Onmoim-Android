package com.onmoim.core.dispatcher

import javax.inject.Qualifier

@Target(
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION
)
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val onmoimDispatcher: OnmoimDispatcher)

enum class OnmoimDispatcher {
    DEFAULT, IO
}