package com.onmoim.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.onmoim.core.data.constant.BoardCategory
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.model.Comment
import com.onmoim.core.data.model.CommentThread
import com.onmoim.core.data.model.Post
import com.onmoim.core.data.pagingsource.CommentPagingSource
import com.onmoim.core.data.pagingsource.CommentThreadPagingSource
import com.onmoim.core.data.pagingsource.PostPagingSource
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.api.PostApi
import com.onmoim.core.network.model.post.CommentRequestDto
import com.onmoim.core.network.model.post.CreatePostRequestDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
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

    override suspend fun createPost(
        groupId: Int,
        boardCategory: BoardCategory,
        title: String,
        content: String,
        imagePaths: List<String>
    ): Result<Unit> {
        val createPostRequestDto = CreatePostRequestDto(
            type = when (boardCategory) {
                BoardCategory.NOTICE -> "NOTICE"
                BoardCategory.INTRODUCTION -> "INTRODUCTION"
                BoardCategory.REVIEW -> "REVIEW"
                BoardCategory.FREE -> "FREE"
            },
            title = title,
            content = content
        )
        val requestBody = Json.encodeToString(createPostRequestDto)
            .toRequestBody("application/json".toMediaTypeOrNull())
        val imageFileParts = imagePaths.map {
            val file = File(it)
            MultipartBody.Part.createFormData(
                "files",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }.ifEmpty { null }

        val resp = withContext(ioDispatcher) {
            postApi.createPost(groupId, requestBody, imageFileParts)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(resp.message()))
        }
    }

    override fun getPost(
        groupId: Int,
        postId: Int
    ): Flow<Post> = flow {
        val resp = postApi.getPost(groupId, postId)
        val data = resp.body()?.data

        if (resp.isSuccessful && data != null) {
            val systemZoneId = ZoneId.systemDefault()
            val createdLocalDateTimeUTC = LocalDateTime.parse(data.createdDate)
            val modifiedLocalDateTimeUTC = LocalDateTime.parse(data.modifiedDate)
            val createdZonedDateTime = createdLocalDateTimeUTC.atZone(systemZoneId)
            val modifiedZonedDateTime = modifiedLocalDateTimeUTC.atZone(systemZoneId)

            val post = Post(
                id = data.id,
                title = data.title,
                content = data.content,
                name = data.authorName,
                profileImageUrl = data.authorProfileImage,
                type = PostType.valueOf(data.type),
                createdDate = createdZonedDateTime.toLocalDateTime(),
                modifiedDate = modifiedZonedDateTime.toLocalDateTime(),
                imageUrls = data.imageUrls,
                likeCount = data.likeCount,
                isLiked = data.isLiked,
                commentCount = 0 // FIXME: api 수정되면 확인
            )
            emit(post)
        } else {
            throw Exception(resp.message())
        }
    }.flowOn(ioDispatcher)

    override fun getCommentPagingData(
        groupId: Int,
        postId: Int,
        size: Int
    ): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CommentPagingSource(postApi, groupId, postId) }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun likePost(
        groupId: Int,
        postId: Int
    ): Result<Boolean> {
        val resp = withContext(ioDispatcher) {
            postApi.likePost(groupId, postId)
        }
        val data = resp.body()?.data

        return if (resp.isSuccessful && data != null) {
            Result.success(data.isLiked)
        } else {
            Result.failure(Exception(resp.message()))
        }
    }

    override suspend fun writeComment(
        groupId: Int,
        postId: Int,
        content: String
    ): Result<Unit> {
        val commentRequestDto = CommentRequestDto(content)
        val resp = withContext(ioDispatcher) {
            postApi.createComment(groupId, postId, commentRequestDto)
        }

        return if (resp.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(resp.message()))
        }
    }

    override fun getCommentThreadPagingData(
        groupId: Int,
        postId: Int,
        commentId: Int,
        size: Int
    ): Flow<PagingData<CommentThread>> {
        return Pager(
            config = PagingConfig(
                pageSize = size,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CommentThreadPagingSource(postApi, groupId, postId, commentId) }
        ).flow.flowOn(ioDispatcher)
    }

    override suspend fun updateComment(
        groupId: Int,
        postId: Int,
        commentId: Int,
        content: String
    ) {
        val commentRequestDto = CommentRequestDto(content)
        val resp = withContext(ioDispatcher) {
            postApi.updateComment(groupId, postId, commentId, commentRequestDto)
        }

        if (!resp.isSuccessful) {
            throw Exception(resp.message())
        }
    }

    override suspend fun deleteComment(
        groupId: Int,
        postId: Int,
        commentId: Int
    ) {
        val resp = withContext(ioDispatcher) {
            postApi.deleteComment(groupId, postId, commentId)
        }

        if (!resp.isSuccessful) {
            throw Exception(resp.message())
        }
    }
}