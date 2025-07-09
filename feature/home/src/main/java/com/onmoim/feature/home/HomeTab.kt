package com.onmoim.feature.home

enum class HomeTab(
    val labelId: Int
) {
    RECOMMEND(
        labelId = R.string.home_recommend_meet
    ),
    POPULARITY(
        labelId = R.string.home_popularity_meet
    ),
    FAVORITE(
        labelId = R.string.home_favorite_meet
    ),
}