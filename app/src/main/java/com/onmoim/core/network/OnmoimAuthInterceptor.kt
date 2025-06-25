package com.onmoim.core.network

import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OnmoimAuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val accessToken = runBlocking(ioDispatcher) {
            tokenRepository.getAccessToken()
        }
        val authReq = req.newBuilder().apply {
            header("Authorization", "Bearer $accessToken")
        }.build()
        return chain.proceed(authReq)
    }
}