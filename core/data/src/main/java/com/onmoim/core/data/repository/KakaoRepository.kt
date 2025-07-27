package com.onmoim.core.data.repository

import com.onmoim.core.data.model.Place
import kotlinx.coroutines.flow.Flow

interface KakaoRepository {
    fun searchKeyword(query: String): Flow<List<Place>>
}