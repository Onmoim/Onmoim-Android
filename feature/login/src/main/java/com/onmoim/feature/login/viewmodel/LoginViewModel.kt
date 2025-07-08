package com.onmoim.feature.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.data.constant.AccountStatus
import com.onmoim.core.domain.usecase.SignInUseCase
import com.onmoim.core.helper.SocialSignInHelper
import com.onmoim.core.helper.constant.SocialType
import com.onmoim.feature.login.state.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialSignInHelper: SocialSignInHelper,
    private val signInUseCase: SignInUseCase
) : ViewModel() {
    private val tag = "LoginViewModel"

    private val _event = Channel<LoginEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun signIn(type: SocialType) {
        viewModelScope.launch {
            socialSignInHelper.signIn(type).flatMapConcat { token ->
                requireNotNull(token) {
                    "token이 null임."
                }
                val provider = when (type) {
                    SocialType.GOOGLE -> "google"
                    SocialType.KAKAO -> "kakao"
                }
                signInUseCase(provider, token)
            }.catch {
                Log.e(tag, "로그인 에러", it)
                sendEvent(LoginEvent.ShowErrorDialog(it))
            }.collectLatest { status ->
                when (status) {
                    AccountStatus.EXISTS -> {
                        _event.send(LoginEvent.NavigateToHome)
                    }

                    AccountStatus.NOT_EXISTS -> {
                        _event.send(LoginEvent.NavigateToProfileSetting)
                    }

                    AccountStatus.NO_CATEGORY -> {
                        _event.send(LoginEvent.NavigateToInterestSelect)
                    }
                }
            }
        }
    }
}