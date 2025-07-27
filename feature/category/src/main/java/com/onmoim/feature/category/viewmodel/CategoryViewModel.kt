package com.onmoim.feature.category.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.onmoim.core.data.model.Category
import com.onmoim.core.data.model.Group
import com.onmoim.core.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    private val _categories =
        MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private var _selectedCategoryIdState = MutableStateFlow(0)
    val selectedCategoryIdState = _selectedCategoryIdState.asStateFlow()

    private val _groupsByCategoryPagingDataMapState =
        MutableStateFlow<Map<Int, Flow<PagingData<Group>>>>(emptyMap())
    val groupsByCategoryPagingDataMapState = _groupsByCategoryPagingDataMapState.asStateFlow()

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories().retryWhen { cause, attempt ->
                delay(1000)
                attempt < 3
            }.catch {
                Log.e("CategoryViewModel", "fetchCategories: $it")
            }.collect {
                _categories.value = it
                if (_selectedCategoryIdState.value == 0) {
                    onSelectedCategoryChange(it.first().id)
                }
            }
        }
    }

    fun onSelectedCategoryChange(id: Int) {
        _selectedCategoryIdState.value = id

        viewModelScope.launch {
            _groupsByCategoryPagingDataMapState.update { state ->
                if (state.containsKey(id)) {
                    state
                } else {
                    state + (id to categoryRepository.getGroupsByCategoryPagingData(id)
                        .cachedIn(viewModelScope))
                }
            }
        }
    }
}
