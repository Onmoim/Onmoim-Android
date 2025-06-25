package com.onmoim.core.data.repository

interface TokenRepository {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun setJwt(accessToken: String, refreshToken: String?)
    suspend fun clearJwt()
}