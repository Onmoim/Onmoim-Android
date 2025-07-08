package com.onmoim.core.helper.di

import android.content.Context
import com.onmoim.core.helper.SocialSignInHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelperModule {

    @Provides
    @Singleton
    fun provideSocialSignInHelper(
        @ApplicationContext context: Context
    ) = SocialSignInHelper(context)
}