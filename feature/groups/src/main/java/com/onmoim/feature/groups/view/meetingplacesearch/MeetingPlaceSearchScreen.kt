package com.onmoim.feature.groups.view.meetingplacesearch

import android.Manifest
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.onmoim.core.designsystem.component.CommonAppBar
import com.onmoim.core.designsystem.component.NavigationIconButton
import com.onmoim.core.designsystem.theme.OnmoimTheme
import com.onmoim.core.ui.NaverMap
import com.onmoim.core.util.checkLocationPermission
import com.onmoim.core.util.findActivity
import com.onmoim.feature.groups.R
import com.onmoim.feature.groups.viewmodel.MeetingPlaceSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingPlaceSearchRoute(
    meetingPlaceSearchViewModel: MeetingPlaceSearchViewModel = hiltViewModel(),
    onBackAndSendPlaceInfo: (placeName: String, latitude: Double, longitude: Double) -> Unit
) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val context = LocalContext.current
    val searchKeyword by meetingPlaceSearchViewModel.searchKeyword.collectAsStateWithLifecycle()
    var naverMap: NaverMap? by remember {
        mutableStateOf(null)
    }
    var hasLocationPermission by remember { mutableStateOf(context.checkLocationPermission()) }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    val locationPermissions = remember {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionMap ->
            hasLocationPermission = permissionMap.values.all { it }
        }
    )
    var isMovedCamera by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        PlaceInfoModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            title = "장소 이름",
            address = "주소",
            phoneNumber = "02-1234-5678",
            onClickSetting = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
                onBackAndSendPlaceInfo("장소 이름", 37.5670135, 126.9783740)
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MeetingPlaceSearchScreen(
            onBack = {
                onBackPressedDispatcher?.onBackPressed()
            },
            searchKeyword = searchKeyword,
            onSearchKeywordChange = meetingPlaceSearchViewModel::onSearchKeywordChange,
            onSearch = {
                meetingPlaceSearchViewModel.fetchPlaceSearch(searchKeyword)
            }
        ) {
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                onMapReady = {
                    naverMap = it

                    val uiSettings = it.uiSettings
                    uiSettings.isZoomControlEnabled = false
                    uiSettings.isCompassEnabled = false
                    uiSettings.isRotateGesturesEnabled = false

                    val locationOverlay = it.locationOverlay
                    locationOverlay.icon = OverlayImage.fromResource(R.drawable.ic_current_location)
                }
            )
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .navigationBarsPadding()
                .align(Alignment.BottomCenter)
        )
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        scope.launch {
            delay(30)
            hasLocationPermission = context.checkLocationPermission()
        }
    }

    DisposableEffect(hasLocationPermission, fusedLocationClient) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    userLocation = location
                }
            }
        }

        when {
            hasLocationPermission -> {
                val locationRequest =
                    LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }

            !ActivityCompat.shouldShowRequestPermissionRationale(
                context.findActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || !ActivityCompat.shouldShowRequestPermissionRationale(
                context.findActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                scope.launch {
                    val result = snackBarHostState.showSnackbar(
                        message = context.getString(R.string.meeting_place_search_permission_guide),
                        actionLabel = ContextCompat.getString(context, R.string.setting),
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }.run {
                            context.startActivity(this)
                        }
                    }
                }
            }

            else -> {
                requestPermissionLauncher.launch(locationPermissions)
            }
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    LaunchedEffect(naverMap) {
        if (naverMap != null) {
            val icon = OverlayImage.fromResource(R.drawable.ic_marker)
            val marker = Marker()
            marker.position = LatLng(37.5670135, 126.9783740)
            marker.icon = icon
            marker.captionText = "테스트 마커"
            marker.onClickListener = Overlay.OnClickListener { overlay ->
                showBottomSheet = true
                true
            }
            marker.map = naverMap
        }
    }

    LaunchedEffect(naverMap, userLocation) {
        if (naverMap != null && userLocation != null) {
            naverMap!!.apply {
                val coord = LatLng(userLocation!!.latitude, userLocation!!.longitude)

                locationOverlay.isVisible = true
                locationOverlay.position = coord
                locationOverlay.bearing = userLocation!!.bearing

                if (!isMovedCamera) {
                    val cameraUpdate = CameraUpdate.scrollTo(coord).animate(CameraAnimation.Easing)
                    moveCamera(cameraUpdate)
                    isMovedCamera = true
                }
            }
        }
    }
}

@Composable
private fun MeetingPlaceSearchScreen(
    onBack: () -> Unit,
    searchKeyword: String,
    onSearchKeywordChange: (String) -> Unit,
    onSearch: () -> Unit,
    mapContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(OnmoimTheme.colors.backgroundColor)
            .fillMaxSize()
    ) {
        CommonAppBar(
            title = {
                Text(
                    text = stringResource(R.string.meeting_place_search),
                    style = OnmoimTheme.typography.body1SemiBold
                )
            },
            navigationIcon = {
                NavigationIconButton(
                    onClick = onBack
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = null
                    )
                }
            }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            mapContent()
            SearchTextField(
                value = searchKeyword,
                onValueChange = onSearchKeywordChange,
                modifier = Modifier
                    .padding(
                        horizontal = 15.dp,
                        vertical = 16.dp
                    )
                    .fillMaxWidth(),
                onSearch = {
                    onSearch()
                }
            )
        }
    }
}

@Preview
@Composable
private fun MeetingLocationSearchScreenPreview() {
    OnmoimTheme {
        MeetingPlaceSearchScreen(
            onBack = {},
            searchKeyword = "",
            onSearchKeywordChange = {},
            onSearch = {},
            mapContent = {}
        )
    }
}