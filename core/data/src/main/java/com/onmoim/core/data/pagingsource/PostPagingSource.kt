package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.constant.PostType
import com.onmoim.core.data.model.Post
import com.onmoim.core.network.api.PostApi
import java.time.Instant
import java.time.ZoneId

class PostPagingSource(
    private val postApi: PostApi,
    private val groupId: Int,
    private val type: PostType
) : PagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        return try {
            val key = params.key
            val resp = postApi.getPosts(
                groupId = groupId,
                type = when (type) {
                    PostType.ALL -> "ALL"
                    PostType.NOTICE -> "NOTICE"
                    PostType.INTRODUCTION -> "INTRODUCTION"
                    PostType.REVIEW -> "REVIEW"
                    PostType.FREE -> "FREE"
                },
                cursorId = key,
                size = params.loadSize
            )
            val data = resp.body()?.data
            val systemZoneId = ZoneId.systemDefault()
            val posts = data?.content?.map {
                val createdInstant = Instant.parse(it.createdDate)
                val modifiedInstant = Instant.parse(it.modifiedDate)
                val createdZonedDateTime = createdInstant.atZone(systemZoneId)
                val modifiedZonedDateTime = modifiedInstant.atZone(systemZoneId)

                Post(
                    id = it.id,
                    title = it.title,
                    content = it.content,
                    name = it.authorName,
                    profileImageUrl = it.authorProfileImage,
                    type = PostType.valueOf(it.type),
                    createdDate = createdZonedDateTime.toLocalDateTime(),
                    modifiedDate = modifiedZonedDateTime.toLocalDateTime(),
                    imageUrls = it.imageUrls,
                    likeCount = it.likeCount,
                    isLiked = it.isLiked
                )
            } ?: emptyList()
            val nextCursorId = data?.nextCursorId
            val nextKey = if (data?.hasNext == true) nextCursorId else null
            LoadResult.Page(data = posts, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("PostPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}