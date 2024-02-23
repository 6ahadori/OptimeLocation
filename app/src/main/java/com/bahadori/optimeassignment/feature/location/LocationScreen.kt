package com.bahadori.optimeassignment.feature.location

import android.location.Location
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bahadori.optimeassignment.R
import com.bahadori.optimeassignment.feature.location.components.Location


@Composable
fun LocationRoute(
    viewModel: LocationViewModel = hiltViewModel()
) {
    val locations = viewModel.locations.collectAsStateWithLifecycle().value
    LocationScreen(locations)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationScreen(locations: List<Location>) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(locations) {
                    Location(location = it)
                }
            }
            if (locations.isEmpty()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.there_is_no_location_data_yet)
                )
            }
        }
    }

}

@Preview
@Composable
private fun LocationScreenPreview() {
    LocationScreen(emptyList())
}