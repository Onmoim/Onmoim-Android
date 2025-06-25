package com.onmoim.core.dispatcher

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Dispatcher(OnmoimDispatcher.DEFAULT)
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Dispatcher(OnmoimDispatcher.IO)
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}