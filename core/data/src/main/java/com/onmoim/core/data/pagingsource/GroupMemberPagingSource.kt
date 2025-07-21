package com.onmoim.core.data.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.onmoim.core.data.constant.GroupMemberRole
import com.onmoim.core.data.model.Member
import com.onmoim.core.network.api.GroupApi

class GroupMemberPagingSource(
    private val groupApi: GroupApi,
    private val groupId: Int
) : PagingSource<Int, Member>() {
    override fun getRefreshKey(state: PagingState<Int, Member>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Member> {
        return try {
            val key = params.key
            val resp = groupApi.getGroupMembers(
                groupId = groupId,
                lastMemberId = key,
                requestSize = params.loadSize
            )
            val data = resp.body()?.data
            val members = data?.content?.map {
                Member(
                    id = it.memberId,
                    name = it.username,
                    profileImageUrl = it.profileImageUrl,
                    role = when {
                        it.role.contains("모임장") -> GroupMemberRole.OWNER
                        else -> GroupMemberRole.MEMBER
                    }
                )
            } ?: emptyList()
            val lastMemberId = data?.extraInfo?.lastMemberId
            val nextKey = if (data?.extraInfo?.hasNext == true) lastMemberId else null
            LoadResult.Page(data = members, nextKey = nextKey, prevKey = null)
        } catch (e: Exception) {
            Log.e("GroupMemberPagingSource", "load error", e)
            LoadResult.Error(e)
        }
    }
}