package com.onmoim.feature.home.constant

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeGroupType {
    sealed class Recommend: HomeGroupType() {
        data object Similar: Recommend()
        data object Nearby: Recommend()
    }
    sealed class Popular: HomeGroupType() {
        data object Nearby: Popular()
        data object Active: Popular()
    }
}