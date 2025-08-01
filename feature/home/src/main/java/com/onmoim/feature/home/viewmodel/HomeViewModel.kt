package com.onmoim.feature.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.constant.HomeRecommend
import com.onmoim.core.data.repository.GroupRepository
import com.onmoim.feature.home.constant.HomeTab
import com.onmoim.feature.home.state.HomePopularGroupUiState
import com.onmoim.feature.home.state.HomeRecommendGroupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {
    private val _recommendGroupUiState =
        MutableStateFlow<HomeRecommendGroupUiState>(HomeRecommendGroupUiState.Loading)
    val recommendGroupUiState = _recommendGroupUiState.asStateFlow()

    private val _popularGroupUiState =
        MutableStateFlow<HomePopularGroupUiState>(HomePopularGroupUiState.Loading)
    val popularGroupUiState = _popularGroupUiState.asStateFlow()

    val favoriteGroupPagingData =
        groupRepository.getFavoriteGroupPagingData().cachedIn(viewModelScope)

    private val _selectedTabState = MutableStateFlow(HomeTab.RECOMMEND)
    val selectedTabState = _selectedTabState.asStateFlow()

    init {
        fetchRecommendGroups()
        fetchPopularGroups()
    }

    fun fetchRecommendGroups() {
        viewModelScope.launch {
            combine(
                groupRepository.getHomeRecommendGroups(HomeRecommend.CATEGORY),
                groupRepository.getHomeRecommendGroups(HomeRecommend.LOCATION)
            ) { categoryGroups, locationGroups ->
                HomeRecommendGroupUiState.Success(categoryGroups, locationGroups)
            }.catch {
                Log.e("HomeViewModel", "fetchRecommendGroups error", it)
                _recommendGroupUiState.value = HomeRecommendGroupUiState.Error(it)
            }.collectLatest {
                _recommendGroupUiState.value = it
            }
        }
    }

    fun fetchPopularGroups() {
        viewModelScope.launch {
            combine(
                groupRepository.getHomePopularGroups(HomePopular.NEARBY),
                groupRepository.getHomePopularGroups(HomePopular.ACTIVE)
            ) { nearbyGroups, activeGroups ->
                HomePopularGroupUiState.Success(nearbyGroups, activeGroups)
            }.catch {
                Log.e("HomeViewModel", "fetchPopularGroups error", it)
                _popularGroupUiState.value = HomePopularGroupUiState.Error(it)
            }.collectLatest {
                _popularGroupUiState.value = it
            }
        }
    }

    fun onSelectedTabChange(value: HomeTab) {
        _selectedTabState.value = value
    }
}