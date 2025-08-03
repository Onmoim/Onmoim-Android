package com.onmoim.core.data.di

import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.AppSettingRepositoryImpl
import com.onmoim.core.data.repository.AuthRepository
import com.onmoim.core.data.repository.AuthRepositoryImpl
import com.onmoim.core.data.repository.CategoryRepository
import com.onmoim.core.data.repository.CategoryRepositoryImpl
import com.onmoim.core.data.repository.ChatRepository
import com.onmoim.core.data.repository.ChatRepositoryImpl
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.core.data.repository.GroupRepositoryImpl
import com.onmoim.core.data.repository.KakaoRepository
import com.onmoim.core.data.repository.KakaoRepositoryImpl
import com.onmoim.core.data.repository.LocationRepository
import com.onmoim.core.data.repository.LocationRepositoryImpl
import com.onmoim.core.data.repository.MeetingRepository
import com.onmoim.core.data.repository.MeetingRepositoryImpl
import com.onmoim.core.data.repository.PostRepository
import com.onmoim.core.data.repository.PostRepositoryImpl
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.data.repository.TokenRepositoryImpl
import com.onmoim.core.data.repository.UserRepository
import com.onmoim.core.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    abstract fun bindTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindAppSettingRepository(
        appSettingRepositoryImpl: AppSettingRepositoryImpl
    ): AppSettingRepository

    @Binds
    abstract fun bindGroupRepository(
        groupRepositoryImpl: GroupRepositoryImpl
    ): GroupRepository

    @Binds
    abstract fun bindMeetingRepository(
        meetingRepositoryImpl: MeetingRepositoryImpl
    ): MeetingRepository

    @Binds
    abstract fun bindKakaoRepository(
        kakaoRepositoryImpl: KakaoRepositoryImpl
    ): KakaoRepository

    @Binds
    abstract fun bindPostRepository(
        postRepositoryImpl: PostRepositoryImpl
    ): PostRepository

    @Binds
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository
}