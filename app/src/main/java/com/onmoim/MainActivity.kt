package com.onmoim

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.rememberNavController
import com.onmoim.core.data.repository.AppSettingRepository
import com.onmoim.core.data.repository.TokenRepository
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.event.AuthEventBus
import com.onmoim.ui.OnmoimApp
import com.onmoim.ui.rememberOnmoimAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    lateinit var appSettingRepository: AppSettingRepository

    @Inject
    lateinit var authEventBus: AuthEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberOnmoimAppState(
                navController = rememberNavController(),
                coroutineScope = rememberCoroutineScope(),
                tokenRepository = tokenRepository,
                appSettingRepository = appSettingRepository,
                authEventBus = authEventBus
            )

            DisposableEffect(Unit) {
                val listener = ViewTreeObserver.OnPreDrawListener {
                    !appState.shouldShowSplash
                }
                val content: View = findViewById(android.R.id.content)
                content.viewTreeObserver.addOnPreDrawListener(listener)

                onDispose {
                    content.viewTreeObserver.removeOnPreDrawListener(listener)
                }
            }

            OnmoimTheme {
                OnmoimApp(
                    appState = appState
                )
            }
        }
    }
}