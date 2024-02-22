package com.bahadori.optimeassignment.core.data.service

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.bahadori.optimeassignment.core.common.Constants.LOCATION_INTERVAL
import com.bahadori.optimeassignment.core.common.Constants.TAG
import com.bahadori.optimeassignment.core.domain.repository.LocationRepository
import com.bahadori.optimeassignment.core.notification.NotificationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var locationRepository: LocationRepository

    @Inject
    lateinit var notificationHelper: NotificationHelper

    private val locationCallback: (Location?) -> Unit = { location ->
        if (location != null) {
            locationRepository.onLocationUpdated(location)
            notificationHelper.showNotification("Current Location", location.latitude.toString())
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ACTION_STOP_SELF == intent?.action) {
            stopForeground(true)
            stopSelf()
        } else {
            notificationHelper.startForeground(this)
            val locationRequest = LocationRequest.Builder(LOCATION_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return super.onStartCommand(intent, flags, startId)
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        const val ACTION_STOP_SELF = "stop.self"
    }
}