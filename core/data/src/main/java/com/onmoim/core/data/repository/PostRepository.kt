package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.BoardCategory
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostPagingData(groupId: Int, type: PostType, size: Int = 20): Flow<PagingData<Post>>
    suspend fun createPost(
        groupId: Int,
        boardCategory: BoardCategory,
        title: String,
        content: String,
        imagePaths: List<String>
    ): Result<Unit>
}