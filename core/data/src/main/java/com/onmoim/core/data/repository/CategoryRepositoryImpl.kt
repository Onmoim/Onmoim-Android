package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Category
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.CategoryApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryApi: CategoryApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
): CategoryRepository {
    override fun getCategories(): Flow<List<Category>> = flow {
        val resp = categoryApi.getCategories()
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val categories = data.map {
                Category(
                    id = it.categoryId,
                    name = it.name,
                    imageUrl = it.iconUrl
                )
            }
            emit(categories)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)
}