package com.onmoim.core.network

import android.util.Log
import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.datastore.PreferencesKey
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import com.onmoim.core.event.AuthEventBus
import com.onmoim.core.network.model.BaseResponse
import com.onmoim.core.network.model.auth.ReissueTokenRequest
import com.onmoim.core.network.model.auth.TokenDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class OnmoimAuthenticator @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
    private val authEventBus: AuthEventBus,
    @ApiType(OnmoimApiType.DEFAULT) private val client: OkHttpClient,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val authorizationHeader = response.request.header("Authorization")
        val hasBearerToken = authorizationHeader?.startsWith("Bearer ") == true

        return if (hasBearerToken && response.retryCount() < 3) {
            synchronized(this) {
                val currentAccessToken = runBlocking(ioDispatcher) {
                    dataStorePreferences.getString(PreferencesKey.ACCESS_TOKEN).first()
                }
                val failedRequestToken =
                    response.request.header("Authorization")?.substringAfter("Bearer ")

                if (currentAccessToken != null && failedRequestToken != currentAccessToken) {
                    return response.request.newBuilder().apply {
                        header("Authorization", "Bearer $currentAccessToken")
                    }.build()
                }

                val refreshToken = runBlocking(ioDispatcher) {
                    dataStorePreferences.getString(PreferencesKey.REFRESH_TOKEN).first()
                }
                if (refreshToken == null) {
                    runBlocking(ioDispatcher) {
                        authEventBus.notifyAuthExpired()
                    }
                    return null
                }

                val refreshResp = try {
                    val refreshReq = Request.Builder().apply {
                        val reissueTokenRequest = ReissueTokenRequest(refreshToken)
                        val json = Json.encodeToString(reissueTokenRequest)
                        val reqBody = json.toRequestBody("application/json".toMediaType())
                        url("${BuildConfig.BASE_URL}/api/v1/auth/reissue-tkn")
                        post(reqBody)
                    }.build()
                    client.newCall(refreshReq).execute()
                } catch (e: Exception) {
                    Log.e("OnmoimAuthenticator", e.message.toString(), e)
                    runBlocking(ioDispatcher) {
                        authEventBus.notifyAuthExpired()
                    }
                    return null
                }

                if (refreshResp.isSuccessful) {
                    val respJson = refreshResp.body.string()
                    val json = Json { ignoreUnknownKeys = true }
                    val respData = json.decodeFromString<BaseResponse<TokenDto>>(respJson)
                    val token = respData.data ?: return null

                    runBlocking(ioDispatcher) {
                        dataStorePreferences.putString(
                            PreferencesKey.ACCESS_TOKEN,
                            token.accessToken
                        )
                        token.refreshToken?.let {
                            dataStorePreferences.putString(
                                PreferencesKey.REFRESH_TOKEN,
                                it
                            )
                        }
                    }

                    return response.request.newBuilder().apply {
                        header("Authorization", "Bearer ${token.accessToken}")
                    }.build()
                } else {
                    runBlocking(ioDispatcher) {
                        authEventBus.notifyAuthExpired()
                    }
                    return null
                }
            }
        } else {
            runBlocking(ioDispatcher) {
                authEventBus.notifyAuthExpired()
            }
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