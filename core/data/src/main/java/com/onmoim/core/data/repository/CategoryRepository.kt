package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
}