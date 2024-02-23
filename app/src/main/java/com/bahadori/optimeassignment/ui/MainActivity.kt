package com.bahadori.optimeassignment.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import com.bahadori.optimeassignment.core.common.ext.showMessage
import com.bahadori.optimeassignment.core.data.service.LocationService
import com.bahadori.optimeassignment.core.designsystem.theme.OptimeAssignmentTheme
import com.bahadori.optimeassignment.core.domain.usecase.IsGpsOn
import com.bahadori.optimeassignment.feature.location.LocationRoute
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var isGpsOn: IsGpsOn

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isGpsOn = IsGpsOn(activityResultRegistry, this)
        lifecycle.addObserver(isGpsOn)

        setContent {
            val locationPermissionState = rememberMultiplePermissionsState(
                mutableListOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ).apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        add(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            )

            LaunchedEffect(locationPermissionState.allPermissionsGranted) {
                if (!locationPermissionState.allPermissionsGranted) {
                    locationPermissionState.launchMultiplePermissionRequest()
                } else {
                    checkGps()
                }

            }

            OptimeAssignmentTheme {
                LocationRoute()
            }
        }
    }

    private fun startLocationService() {
        startService(Intent(this, LocationService::class.java))
    }

    private fun checkGps() {
        lifecycleScope.launch {
            isGpsOn().collect { result ->
                if (result.isSuccess) {
                    startLocationService()
                }

                if (result.isFailure) {
                    showMessage("Please turn on the location for app functionality!")
                }
            }
        }
    }
}