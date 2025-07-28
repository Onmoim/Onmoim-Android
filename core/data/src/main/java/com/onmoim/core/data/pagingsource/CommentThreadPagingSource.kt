package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.model.Comment
import com.onmoim.core.data.model.CommentThread
import com.onmoim.core.network.api.PostApi
import java.time.LocalDateTime
import java.time.ZoneId

class CommentThreadPagingSource(
    private val postApi: PostApi,
    private val groupId: Int,
    private val postId: Int,
    private val commentId: Int
) : PagingSource<Int, CommentThread>() {
    override fun getRefreshKey(state: PagingState<Int, CommentThread>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentThread> {
        return try {
            val key = params.key
            val resp = postApi.getCommentThread(
                groupId = groupId,
                postId = postId,
                commentId = commentId,
                cursor = key,
//                size = params.loadSize // FIXME: api 수정되면 확인
            )
            val data = resp.body()?.data
            val systemZoneId = ZoneId.systemDefault()
            val parentComment = data?.parentComment?.let {
                val createdLocalDateTimeUTC = LocalDateTime.parse(it.createdAt)
                val modifiedLocalDateTimeUTC = LocalDateTime.parse(it.updatedAt)
                val createdZonedDateTime = createdLocalDateTimeUTC.atZone(systemZoneId)
                val modifiedZonedDateTime = modifiedLocalDateTimeUTC.atZone(systemZoneId)

                Comment(
                    id = it.id,
                    authorId = 0, // FIXME: api 수정되면 확인
                    userName = it.authorName,
                    profileImageUrl = it.authorProfileImg,
                    content = it.content,
                    createdDate = createdZonedDateTime.toLocalDateTime(),
                    modifiedDate = modifiedZonedDateTime.toLocalDateTime(),
                    replyCount = it.replyCount,
                )
            } ?: throw Exception("parentComment is null")
            val replies = data.replies.map {
                val createdLocalDateTimeUTC = LocalDateTime.parse(it.createdAt)
                val modifiedLocalDateTimeUTC = LocalDateTime.parse(it.updatedAt)
                val createdZonedDateTime = createdLocalDateTimeUTC.atZone(systemZoneId)
                val modifiedZonedDateTime = modifiedLocalDateTimeUTC.atZone(systemZoneId)

                Comment(
                    id = it.id,
                    authorId = 0, // FIXME: api 수정되면 확인
                    userName = it.authorName,
                    profileImageUrl = it.authorProfileImg,
                    content = it.content,
                    createdDate = createdZonedDateTime.toLocalDateTime(),
                    modifiedDate = modifiedZonedDateTime.toLocalDateTime(),
                    replyCount = it.replyCount,
                )
            }
            val nextCursor = data.nextCursor
            val nextKey = if (data.hasMore) nextCursor else null
            LoadResult.Page(
                data = if (key == null) {
                    listOf(CommentThread(true, parentComment)) + replies.map {
                        CommentThread(
                            false,
                            it
                        )
                    }
                } else {
                    replies.map { CommentThread(false, it) }
                },
                nextKey = nextKey,
                prevKey = null
            )
        } catch (e: Exception) {
            Log.e("CommentThreadPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}