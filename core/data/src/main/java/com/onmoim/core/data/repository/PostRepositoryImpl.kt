package com.onmoim.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.model.Post
import com.onmoim.core.data.pagingsource.PostPagingSource
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.PostApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postApi: PostApi,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : PostRepository {
    override fun getPostPagingData(
        groupId: Int,
        type: PostType,
        size: Int
    ): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostPagingSource(postApi, groupId, type) }
        ).flow.flowOn(ioDispatcher)
    }
}