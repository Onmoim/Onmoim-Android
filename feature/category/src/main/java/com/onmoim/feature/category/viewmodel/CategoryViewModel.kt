package com.onmoim.feature.category.viewmodel

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
import kotlinx.coroutines.flow.retryWhen
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
                    _selectedCategoryIdState.value = it.first().id
                }
            }
        }
    }

    fun onCategorySelected(id: Int) {
        _selectedCategoryIdState.value = id
    }
}
