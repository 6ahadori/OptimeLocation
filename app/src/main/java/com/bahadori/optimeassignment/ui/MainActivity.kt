package com.bahadori.optimeassignment.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bahadori.optimeassignment.core.data.service.LocationService
import com.bahadori.optimeassignment.core.designsystem.theme.OptimeAssignmentTheme
import com.bahadori.optimeassignment.feature.location.LocationRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startService(Intent(this, LocationService::class.java))

        setContent {
            OptimeAssignmentTheme {
                LocationRoute()
            }
        }
    }
}