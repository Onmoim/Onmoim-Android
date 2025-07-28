package com.onmoim.core.data.repository

import androidx.paging.PagingData
import com.onmoim.core.data.constant.BoardCategory
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.model.Comment
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

    fun getPost(groupId: Int, postId: Int): Flow<Post>
    fun getCommentPagingData(groupId: Int, postId: Int, size: Int = 20): Flow<PagingData<Comment>>
    suspend fun likePost(groupId: Int, postId: Int): Result<Boolean>
    suspend fun writeComment(groupId: Int, postId: Int, content: String): Result<Unit>
}