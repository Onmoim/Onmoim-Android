package com.onmoim.core.network

import com.onmoim.BuildConfig
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.network.model.auth.ReissueTokenRequest
import com.onmoim.core.network.model.auth.TokenDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class OnmoimAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository,
    @HttpClientType(OnmoimHttpClientType.DEFAULT) private val client: OkHttpClient,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val authorizationHeader = response.request.header("Authorization")
        val hasBearerToken = authorizationHeader?.startsWith("Bearer ") == true

        return if (hasBearerToken && response.retryCount() < 3) {
            synchronized(this) {
                val currentAccessToken = runBlocking(ioDispatcher) {
                    tokenRepository.getAccessToken()
                }
                val failedRequestToken =
                    response.request.header("Authorization")?.substringAfter("Bearer ")

                if (currentAccessToken != null && failedRequestToken != currentAccessToken) {
                    return response.request.newBuilder().apply {
                        header("Authorization", "Bearer $currentAccessToken")
                    }.build()
                }

                val refreshToken = runBlocking(ioDispatcher) {
                    tokenRepository.getRefreshToken()
                } ?: return null
                val refreshResp = try {
                    val refreshReq = Request.Builder().apply {
                        val reissueTokenRequest = ReissueTokenRequest(refreshToken)
                        val json = Json.encodeToString(reissueTokenRequest)
                        val reqBody = json.toRequestBody("application/json".toMediaType())
                        url(BuildConfig.BASE_URL)
                        post(reqBody)
                    }.build()
                    client.newCall(refreshReq).execute()
                } catch (e: Exception) {
                    Timber.e(e)
                    return null
                }

                if (refreshResp.isSuccessful) {
                    val respJson = refreshResp.body?.string() ?: return null
                    val respData = Json.decodeFromString<TokenDto>(respJson)

                    runBlocking(ioDispatcher) {
                        tokenRepository.setJwt(respData.accessToken, respData.refreshToken)
                    }

                    return response.request.newBuilder().apply {
                        header("Authorization", "Bearer ${respData.accessToken}")
                    }.build()
                } else {
                    runBlocking(ioDispatcher) {
                        tokenRepository.clearJwt()
                    }
                    return null
                }
            }
        } else {
            null
        }
    }

    private fun Response.retryCount(): Int {
        var currentResponse = this.priorResponse
        var result = 0
        while (currentResponse != null) {
            result++
            currentResponse = currentResponse.priorResponse
        }
        return result
    }
}