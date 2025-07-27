package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.model.Comment
import com.onmoim.core.network.api.PostApi
import java.time.LocalDateTime
import java.time.ZoneId

class CommentPagingSource(
    private val postApi: PostApi,
    private val groupId: Int,
    private val postId: Int
): PagingSource<Int, Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        return try {
            val key = params.key
            val resp = postApi.getComments(
                groupId = groupId,
                postId = postId,
                cursor = key,
//                size = params.loadSize // FIXME: api 수정되면 확인
            )
            val data = resp.body()?.data
            val systemZoneId = ZoneId.systemDefault()
            val comments = data?.content?.map {
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
            } ?: emptyList()
            val nextCursorId = data?.nextCursorId
            val nextKey = if (data?.hasNext == true) nextCursorId else null
            LoadResult.Page(data = comments, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("LikedGroupPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}