package com.onmoim.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.constant.InterestCategory
import com.onmoim.feature.login.state.InterestSelectEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestSelectViewModel @Inject constructor(

): ViewModel() {
    private val _eventChannel = Channel<InterestSelectEvent>(Channel.BUFFERED)
    val receiveEvent = _eventChannel.receiveAsFlow()

    private val _selectedInterestCategories = MutableStateFlow<Set<InterestCategory>>(emptySet())
    val selectedInterestCategories = _selectedInterestCategories.asStateFlow()

    fun onClickCategory(category: InterestCategory) {
        val copySet = _selectedInterestCategories.value.toMutableSet()
        if (copySet.contains(category)) {
            copySet.remove(category)
        } else {
            copySet.add(category)
        }
        _selectedInterestCategories.value = copySet
    }

    fun onClickOk() {
        viewModelScope.launch {
            _eventChannel.send(InterestSelectEvent.Loading)

        }
    }
}