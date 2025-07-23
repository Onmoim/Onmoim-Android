package com.onmoim.feature.groups.viewmodel

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = ComingScheduleViewModel.Factory::class)
class ComingScheduleViewModel @AssistedInject constructor(
    @Assisted("groupId") val groupId: Int?
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("groupId") groupId: Int?): ComingScheduleViewModel
    }


}