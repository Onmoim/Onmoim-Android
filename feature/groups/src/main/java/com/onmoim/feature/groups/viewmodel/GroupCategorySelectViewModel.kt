package com.onmoim.feature.groups.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.model.Category
import com.onmoim.core.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupCategorySelectViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _categoriesState = MutableStateFlow<List<Category>>(emptyList())
    val categoriesState =  _categoriesState.asStateFlow()

    private val _selectedCategoryState = MutableStateFlow<Category?>(null)
    val selectedCategoryState = _selectedCategoryState.asStateFlow()

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            categoryRepository.getCategories().retryWhen { cause, attempt ->
                delay(1000)
                attempt < 3
            }.catch {
                Log.e("GroupCategorySelectViewModel", "fetchCategories error", it)
            }.collectLatest {
                _categoriesState.value = it
            }
        }
    }

    fun onClickCategory(category: Category) {
        _selectedCategoryState.value = category
    }
}