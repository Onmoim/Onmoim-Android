package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun searchLocation(query: String): Flow<List<Location>>
}