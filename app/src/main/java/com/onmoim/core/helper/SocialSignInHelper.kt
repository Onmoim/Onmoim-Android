package com.onmoim.core.helper

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.onmoim.BuildConfig
import com.onmoim.core.constant.SocialType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

class SocialSignInHelper(
    private val context: Context
) {
    fun signIn(type: SocialType): Flow<String?> {
        return when (type) {
            SocialType.GOOGLE -> googleSignIn()
            SocialType.KAKAO -> kakaoSignIn()
        }
    }

    private fun kakaoSignIn(): Flow<String?> = callbackFlow {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            when {
                error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                    Timber.i("카카오 로그인 취소됨.")
                }

                error != null -> {
                    cancel(CancellationException("카카오 로그인 실패", error))
                }

                else -> {
                    trySendBlocking(token?.accessToken).onFailure {
                        Timber.e(it, it?.message.toString())
                    }
                }
            }
            channel.close()
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                when {
                    error is ClientError && error.reason == ClientErrorCause.Cancelled -> {
                        callback(token, error)
                    }

                    error != null -> {
                        Timber.v(error, "카카오톡으로 로그인 실패")
                        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                    }

                    else -> {
                        callback(token, null)
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }

        awaitClose()
    }

    private fun googleSignIn(): Flow<String?> = flow {
        try {
            val credentialManager = CredentialManager.create(context)
            val signInWithGoogleOption =
                GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_SIGN_IN_SERVER_CLIENT_ID).build()
            val getCredRequest = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()
            val result = credentialManager.getCredential(
                context = context,
                request = getCredRequest
            )
            val credential = result.credential as? CustomCredential
            if (credential?.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                emit(googleIdTokenCredential.idToken)
            } else {
                throw Throwable("Unexpected type of credential")
            }
        } catch (e: GetCredentialCancellationException) {
            Timber.i(e, "구글 로그인 취소됨")
        }
    }
}