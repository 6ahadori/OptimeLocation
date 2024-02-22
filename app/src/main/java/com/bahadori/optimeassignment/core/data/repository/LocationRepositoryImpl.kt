package com.bahadori.optimeassignment.core.data.repository

import android.location.Location
import com.bahadori.optimeassignment.core.domain.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor() : LocationRepository {

    private val _locations: MutableStateFlow<List<Location>> = MutableStateFlow(emptyList())
    override val locations: StateFlow<List<Location>> = _locations.asStateFlow()

    override fun onLocationUpdated(location: Location) {
        _locations.update {
            val prevLocations = it.toMutableList()
            prevLocations.add(location)
            prevLocations
        }
    }
}