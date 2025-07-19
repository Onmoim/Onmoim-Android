package com.onmoim.core.data.repository

import com.onmoim.core.data.constant.HomePopular
import com.onmoim.core.data.model.HomeGroup
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getHomePopularGroups(homePopular: HomePopular): Flow<List<HomeGroup>>
}