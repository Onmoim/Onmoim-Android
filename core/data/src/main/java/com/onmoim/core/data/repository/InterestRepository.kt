package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Interest
import kotlinx.coroutines.flow.Flow

interface InterestRepository {
    fun getInterests(): Flow<List<Interest>>
}