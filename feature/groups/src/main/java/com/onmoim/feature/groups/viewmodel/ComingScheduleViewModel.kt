package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.onmoim.core.data.constant.UpcomingMeetingsFilter
import com.onmoim.core.data.model.Meeting
import com.onmoim.core.data.repository.MeetingRepository
import com.onmoim.feature.groups.constant.ComingScheduleFilter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ComingScheduleViewModel.Factory::class)
class ComingScheduleViewModel @AssistedInject constructor(
    @Assisted("groupId") val groupId: Int?,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int?): ComingScheduleViewModel
    }

    private val _comingSchedulePagingDataState =
        MutableStateFlow<Flow<PagingData<Meeting>>>(flowOf(PagingData.empty()))
    val comingSchedulePagingDataState = _comingSchedulePagingDataState.asStateFlow()

    private val _filtersState = MutableStateFlow<Set<ComingScheduleFilter>>(emptySet())
    val filtersState = _filtersState.asStateFlow()

    private val _event = Channel<ComingScheduleEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        fetchComingSchedulePagingData()
    }

    private fun fetchComingSchedulePagingData(
        filters: Set<ComingScheduleFilter> = emptySet(),
        onFailure: () -> Unit = {}
    ) {
        viewModelScope.launch {
            val queryFilters = filters.map {
                when (it) {
                    ComingScheduleFilter.WEEK -> UpcomingMeetingsFilter.WEEK
                    ComingScheduleFilter.MONTH -> UpcomingMeetingsFilter.MONTH
                    ComingScheduleFilter.ATTEND -> UpcomingMeetingsFilter.JOINED
                    ComingScheduleFilter.REGULAR_MEET -> UpcomingMeetingsFilter.REGULAR
                    ComingScheduleFilter.LIGHTNING -> UpcomingMeetingsFilter.FLASH
                }
            }.toSet()
            meetingRepository.getUpcomingMeetingPagingData(queryFilters, groupId).catch {
                Log.e("ComingScheduleViewModel", "fetchComingSchedulePagingData error", it)
                onFailure()
            }.collectLatest {
                _comingSchedulePagingDataState.value = flowOf(it)
            }
        }
    }

    fun onFilterChange(filter: ComingScheduleFilter) {
        val originFilters = _filtersState.value
        val newFilters = if (originFilters.contains(filter)) {
            _filtersState.value - filter
        } else {
            _filtersState.value + filter
        }
        _filtersState.value = newFilters
        fetchComingSchedulePagingData(newFilters) {
            _filtersState.value = originFilters
        }
    }

    fun clearFilter() {
        val originFilters = _filtersState.value
        _filtersState.value = emptySet()
        fetchComingSchedulePagingData {
            _filtersState.value = originFilters
        }
    }

    fun attendMeeting(meetingId: Int, groupId: Int) {

    }

    fun leaveMeeting(meetingId: Int, groupId: Int) {

    }
}

sealed class ComingScheduleEvent {
    data class FilterChangeAndLoadDateFailure(val t: Throwable) : ComingScheduleEvent()
}