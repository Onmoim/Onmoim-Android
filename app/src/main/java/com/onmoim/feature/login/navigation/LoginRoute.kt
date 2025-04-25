package com.onmoim.feature.login.navigation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.onmoim.R
import com.onmoim.feature.login.state.LoginEvent
import com.onmoim.feature.login.view.LoginScreen
import com.onmoim.feature.login.viewmodel.LoginViewModel
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
object LoginRoute

@Composable
fun LoginRoute(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val context = LocalContext.current
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
            },
            title = {
                Text(errorMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showErrorDialog = false
                    }
                ) {
                    Text(stringResource(R.string.btn_ok))
                }
            }
        )
    }

    LoginScreen(
        onClickLogin = loginViewModel::signIn
    )

    LaunchedEffect(Unit) {
        loginViewModel.receiveEvent.collect { event ->
            when (event) {
                LoginEvent.NavigateToHome -> onNavigateToHome()
                LoginEvent.NavigateToSignUp -> onNavigateToSignUp()
                is LoginEvent.ShowErrorDialog -> {
                    val error = event.t
                    Timber.e(error, error?.message.toString())
                    errorMessage = ContextCompat.getString(context, R.string.login_error_message)
                    showErrorDialog = true
                }
            }
        }
    }
}