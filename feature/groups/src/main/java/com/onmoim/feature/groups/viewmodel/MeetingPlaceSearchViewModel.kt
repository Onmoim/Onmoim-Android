package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.model.Place
import com.onmoim.core.data.repository.KakaoRepository
import com.onmoim.feature.groups.state.PlaceSearchEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingPlaceSearchViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository
): ViewModel() {
    private val _searchResultState = MutableStateFlow<List<Place>>(emptyList())
    val searchResultState = _searchResultState.asStateFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    private val _event = Channel<PlaceSearchEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        initAutoSearch()
    }

    @OptIn(FlowPreview::class)
    private fun initAutoSearch() {
        searchKeyword.debounce(300L).distinctUntilChanged().onEach { fetchPlaceSearch(it) }
            .launchIn(viewModelScope)
    }

    fun onSearchKeywordChange(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun fetchPlaceSearch(keyword: String) {
        if (keyword.isBlank()) return

        viewModelScope.launch {
            kakaoRepository.searchKeyword(keyword).catch {
                Log.e("PlaceSearchViewModel", "fetchPlaceSearch error", it)
                _event.send(PlaceSearchEvent.SearchFailure(it))
            }.collectLatest {
                _searchResultState.value = it
                if (it.isEmpty()) {
                    _event.send(PlaceSearchEvent.SearchEmpty)
                }
            }
        }
    }
}