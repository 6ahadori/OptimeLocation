package com.bahadori.optimeassignment.feature.location

import android.location.Location
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@Composable
fun LocationRoute(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val locations = viewModel.locations.collectAsStateWithLifecycle().value
    LocationScreen(locations)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationScreen(locations: List<Location>) {
    val locationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        )
    } else {
        rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(locations) {
            Text(text = it.latitude.toString())
        }
    }
}

@Preview
@Composable
private fun LocationScreenPreview() {
    LocationScreen(emptyList())
}