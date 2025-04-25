package com.onmoim.feature.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onmoim.core.constant.SocialType
import com.onmoim.core.helper.SocialSignInHelper
import com.onmoim.feature.login.state.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialSignInHelper: SocialSignInHelper
) : ViewModel() {
    private val _eventChannel = Channel<LoginEvent>(Channel.BUFFERED)
    val receiveEvent = _eventChannel.receiveAsFlow()

    private fun sendEvent(event: LoginEvent) {
        viewModelScope.launch {
            _eventChannel.send(event)
        }
    }

    fun signIn(type: SocialType) {
        viewModelScope.launch {
            socialSignInHelper.signIn(type).catch {
                sendEvent(LoginEvent.ShowErrorDialog(it))
            }.collect {
                // FIXME: 추후 수정 필요
                sendEvent(LoginEvent.NavigateToSignUp)
            }
        }
    }
}