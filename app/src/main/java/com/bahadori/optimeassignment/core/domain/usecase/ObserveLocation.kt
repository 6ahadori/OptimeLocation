package com.bahadori.optimeassignment.core.domain.usecase

import com.bahadori.optimeassignment.core.domain.repository.LocationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveLocation @Inject constructor(
    private val repository: LocationRepository
) {

    operator fun invoke() = repository.locations

}