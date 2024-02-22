package com.bahadori.optimeassignment.core.data.di

import com.bahadori.optimeassignment.core.data.repository.LocationRepositoryImpl
import com.bahadori.optimeassignment.core.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesLocationRepository(): LocationRepository = LocationRepositoryImpl()
}