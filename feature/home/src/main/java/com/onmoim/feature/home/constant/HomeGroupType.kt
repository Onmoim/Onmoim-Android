package com.onmoim.feature.home.constant

import kotlinx.serialization.Serializable

@Serializable
enum class HomeGroupType {
    RECOMMEND_SIMILAR,
    RECOMMEND_NEARBY,
    POPULAR_NEARBY,
    POPULAR_ACTIVE
}