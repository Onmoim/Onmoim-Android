package com.onmoim.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LocationSettingViewModel @Inject constructor(

): ViewModel() {
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

    }
}