package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.model.Category
import com.onmoim.core.data.model.Group
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
    fun getGroupsByCategoryPagingData(categoryId: Int, size: Int = 20): Flow<PagingData<Group>>
}