package com.onmoim.feature.login.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.onmoim.R
import com.onmoim.core.constant.SocialType
import com.onmoim.core.ui.theme.OnmoimTheme
import com.onmoim.core.ui.theme.advancedShadow
import com.onmoim.core.ui.theme.inter
import com.onmoim.core.ui.theme.pretendard
import com.onmoim.core.ui.theme.shadow1
import com.onmoim.feature.login.state.LoginEvent
import com.onmoim.feature.login.viewmodel.LoginViewModel
import timber.log.Timber

@Composable
fun LoginRoute(
    loginViewModel: LoginViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToProfileSetting: () -> Unit
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
                LoginEvent.NavigateToProfileSetting -> onNavigateToProfileSetting()
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

@Composable
private fun LoginScreen(
    onClickLogin: (type: SocialType) -> Unit
) {
    Box(
        modifier = Modifier
            .background(OnmoimTheme.colors.backgroundColor)
            .fillMaxSize()
    ) {
        LoginScreenBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login_title),
                fontFamily = inter,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                color = OnmoimTheme.colors.textColor
            )
            Spacer(Modifier.height(46.dp))
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.padding(vertical = 27.dp)
            )
            Spacer(Modifier.height(64.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.sns_login_title),
                    fontFamily = pretendard,
                    fontWeight = FontWeight.W400,
                    fontSize = 13.sp,
                    color = OnmoimTheme.colors.gray06
                )
                SocialType.entries.forEach { type ->
                    Box(
                        modifier = Modifier
                            .widthIn(min = 240.dp)
                            .shadow1(999.dp)
                            .background(color = type.color, shape = CircleShape)
                            .clip(CircleShape)
                            .clickable {
                                onClickLogin(type)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(type.labelId),
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontFamily = pretendard,
                            fontWeight = FontWeight.W600,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            color = OnmoimTheme.colors.textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginScreenBackground() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .offset(x = (-170).dp, y = (-150).dp)
                .rotate(-91.35f)
                .size(213.5.dp, 231.19.dp)
                .advancedShadow(
                    color = Color(0xFFF061E1),
                    alpha = 0.35f,
                    cornersRadius = 213.5.dp,
                    shadowBlurRadius = 111.4.dp
                )
        )
        Box(
            modifier = Modifier
                .offset(x = (-60).dp, y = (-30).dp)
                .rotate(-91.35f)
                .size(213.5.dp, 231.19.dp)
                .advancedShadow(
                    color = Color(0xFF21C3AB),
                    alpha = 0.35f,
                    cornersRadius = 213.5.dp,
                    shadowBlurRadius = 111.4.dp
                )
        )
        Box(
            modifier = Modifier
                .offset(x = 140.dp, y = 30.dp)
                .rotate(-91.35f)
                .size(213.5.dp, 231.19.dp)
                .advancedShadow(
                    color = Color(0xFF2781FF),
                    alpha = 0.35f,
                    cornersRadius = 213.5.dp,
                    shadowBlurRadius = 111.4.dp
                )
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    OnmoimTheme {
        LoginScreen(
            onClickLogin = {}
        )
    }
}