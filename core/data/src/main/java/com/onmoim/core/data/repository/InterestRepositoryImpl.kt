package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Interest
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.CategoryApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class InterestRepositoryImpl @Inject constructor(
    private val categoryApi: CategoryApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
): InterestRepository {
    override fun getInterests(): Flow<List<Interest>> = flow {
        val resp = categoryApi.getCategories()
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val interests = data.map {
                Interest(
                    id = it.categoryId,
                    name = it.name,
                    imageUrl = it.iconUrl
                )
            }
            emit(interests)
        } else {
            throw HttpException(resp)
        }
    }.flowOn(ioDispatcher)
}