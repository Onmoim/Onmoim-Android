package com.onmoim.core.network

import com.onmoim.core.datastore.DataStorePreferences
import com.onmoim.core.datastore.PreferencesKey
import com.onmoim.core.dispatcher.Dispatcher
import com.onmoim.core.dispatcher.OnmoimDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OnmoimAuthInterceptor @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
    @Dispatcher(OnmoimDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val accessToken = runBlocking(ioDispatcher) {
            dataStorePreferences.getString(PreferencesKey.ACCESS_TOKEN).first()
        }
        val authReq = req.newBuilder().apply {
            header("Authorization", "Bearer $accessToken")
        }.build()
        return chain.proceed(authReq)
    }
}