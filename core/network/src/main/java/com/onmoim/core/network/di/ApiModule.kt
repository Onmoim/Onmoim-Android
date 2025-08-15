package com.onmoim.core.network.di

import com.onmoim.core.network.ApiType
import com.onmoim.core.network.OnmoimApiType
import com.onmoim.core.network.api.AuthApi
import com.onmoim.core.network.api.CategoryApi
import com.onmoim.core.network.api.ChatApi
import com.onmoim.core.network.api.GroupApi
import com.onmoim.core.network.api.KakaoApi
import com.onmoim.core.network.api.LocationApi
import com.onmoim.core.network.api.MeetingApi
import com.onmoim.core.network.api.PostApi
import com.onmoim.core.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideAuthApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    fun provideLocationApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): LocationApi =
        retrofit.create(LocationApi::class.java)

    @Provides
    fun provideUserApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): UserApi =
        retrofit.create(UserApi::class.java)

    @Provides
    fun provideCategoryApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    @Provides
    fun provideGroupApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): GroupApi =
        retrofit.create(GroupApi::class.java)

    @Provides
    fun provideMeetingApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): MeetingApi =
        retrofit.create(MeetingApi::class.java)

    @Provides
    fun provideKakaoApi(@ApiType(OnmoimApiType.KAKAO) retrofit: Retrofit): KakaoApi =
        retrofit.create(KakaoApi::class.java)

    @Provides
    fun providePostApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): PostApi =
        retrofit.create(PostApi::class.java)

    @Provides
    fun provideChatApi(@ApiType(OnmoimApiType.AUTH) retrofit: Retrofit): ChatApi =
        retrofit.create(ChatApi::class.java)
}