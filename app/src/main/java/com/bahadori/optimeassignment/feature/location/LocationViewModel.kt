package com.bahadori.optimeassignment.feature.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bahadori.optimeassignment.core.domain.usecase.ObserveLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class LocationViewModel @Inject constructor(
    observeLocation: ObserveLocation
) : ViewModel() {

    val locations = observeLocation()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )
}