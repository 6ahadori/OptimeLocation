package com.bahadori.optimeassignment.core.domain.repository

import android.location.Location
import kotlinx.coroutines.flow.StateFlow

interface LocationRepository {

    val locations: StateFlow<List<Location>>

    fun onLocationUpdated(location: Location)

}