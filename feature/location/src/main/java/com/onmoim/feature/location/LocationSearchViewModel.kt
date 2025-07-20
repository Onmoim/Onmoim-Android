package com.onmoim.feature.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationSearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<LocationSearchUiState>(LocationSearchUiState.Result(emptyList()))
    val uiState = _uiState.asStateFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    init {
        initAutoSearch()
    }

    @OptIn(FlowPreview::class)
    private fun initAutoSearch() {
        searchKeyword.debounce(300L).distinctUntilChanged().onEach { fetchLocationSearch(it) }
            .launchIn(viewModelScope)
    }

    fun onSearchKeywordChange(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun fetchLocationSearch(keyword: String) {
        if (keyword.isBlank()) return

        viewModelScope.launch {
            locationRepository.searchLocation(keyword).catch {
                _uiState.value = LocationSearchUiState.Error(it)
            }.collectLatest {
                _uiState.value = LocationSearchUiState.Result(it)
            }
        }
    }
}