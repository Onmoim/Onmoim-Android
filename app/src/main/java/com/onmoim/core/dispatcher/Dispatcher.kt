package com.onmoim.core.dispatcher

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Dispatcher(val onmoimDispatcher: OnmoimDispatcher)

enum class OnmoimDispatcher {
    DEFAULT, IO
}