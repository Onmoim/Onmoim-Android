package com.onmoim.core.network

import com.onmoim.core.data.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OnmoimAuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val accessToken = runBlocking(Dispatchers.IO) {
            tokenRepository.getAccessToken()
        }
        val authReq = req.newBuilder().apply {
            header("Authorization", "Bearer $accessToken")
        }.build()
        return chain.proceed(authReq)
    }
}