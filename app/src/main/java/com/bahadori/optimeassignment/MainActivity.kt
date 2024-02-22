package com.bahadori.optimeassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bahadori.optimeassignment.core.designsystem.theme.OptimeAssignmentTheme
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptimeAssignmentTheme {

            }
        }
    }
}