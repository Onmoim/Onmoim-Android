package com.onmoim.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.constant.HomeRecommend
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.home.constant.HomeGroupType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = GroupMoreViewModel.Factory::class)
class GroupMoreViewModel @AssistedInject constructor(
    @Assisted("homeGroupType") val homeGroupType: HomeGroupType,
    groupRepository: GroupRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("homeGroupType") homeGroupType: HomeGroupType
        ): GroupMoreViewModel
    }

    val groupPagingData = when (homeGroupType) {
        HomeGroupType.RECOMMEND_SIMILAR -> groupRepository.getHomeRecommendGroupPagingData(
            homeRecommend = HomeRecommend.CATEGORY
        )

        HomeGroupType.RECOMMEND_NEARBY -> groupRepository.getHomeRecommendGroupPagingData(
            homeRecommend = HomeRecommend.LOCATION
        )

        HomeGroupType.POPULAR_NEARBY -> groupRepository.getHomePopularGroupPagingData(
            homePopular = HomePopular.NEARBY
        )

        HomeGroupType.POPULAR_ACTIVE -> groupRepository.getHomePopularGroupPagingData(
            homePopular = HomePopular.ACTIVE
        )
    }.cachedIn(viewModelScope)
}