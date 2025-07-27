package com.onmoim.core.ui

import android.app.ActivityManager
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap

@Composable
fun NaverMap(
    modifier: Modifier = Modifier,
    onMapReady: (NaverMap) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            layoutParams = LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
    }
    val mapState = rememberSaveable {
        Bundle()
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { view ->
            view.getMapAsync { naverMap -> onMapReady(naverMap) }
        }
    )

    DisposableEffect(Unit) {
        mapView.onCreate(mapState)

        onDispose {
            mapView.onSaveInstanceState(mapState)
            mapView.onDestroy()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        mapView.onStart()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        mapView.onResume()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        mapView.onPause()
    }

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        mapView.onStop()
    }

    LowMemoryEffect {
        mapView.onLowMemory()
    }
}

@Composable
private fun LowMemoryEffect(
    onLowMemory: () -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val callback = object : ComponentCallbacks2 {
            override fun onTrimMemory(level: Int) {
                val isLowMemory = ActivityManager.MemoryInfo().also {
                    activityManager.getMemoryInfo(it)
                }.lowMemory
                if (isLowMemory) {
                    onLowMemory()
                }
            }

            override fun onConfigurationChanged(newConfig: Configuration) {}

            override fun onLowMemory() {
                onLowMemory()
            }

        }
        context.registerComponentCallbacks(callback)

        onDispose {
            context.unregisterComponentCallbacks(callback)
        }
    }
}