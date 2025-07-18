package com.onmoim.core.data.model

data class HomeGroup(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val location: String,
    val memberCount: Int,
    val scheduleCount: Int,
    val categoryName: String
)
