package com.bahadori.optimeassignment.core.domain.usecase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import com.bahadori.optimeassignment.core.common.ext.checkLocationPermission
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class IsGpsOn(
    private val registry: ActivityResultRegistry,
    private val context: Context
) : DefaultLifecycleObserver {

    operator fun invoke() = callbackFlow<Result<Unit>> {
        // checking permission
        if (context.checkLocationPermission()) {
            close()
        }

        // resultLauncher for GPS
        val registration =
            registry.register(
                "gps",
                ActivityResultContracts.StartIntentSenderForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    trySend(Result.success(Unit))
                } else {
                    trySend(Result.failure(Exception()))
                }
            }

        // provide a location request
        val locationRequest = LocationRequest.Builder(10_000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).build()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(context)
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(builder.build())

        // check the success
        task.addOnSuccessListener {
            trySend(Result.success(Unit))
        }

        // check the failure
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val request: IntentSenderRequest = IntentSenderRequest.Builder(
                        exception.resolution.intentSender
                    ).setFillInIntent(Intent())
                        .setFlags(0, 0)
                        .build()
                    registration.launch(request)
                } catch (_: IntentSender.SendIntentException) {
                }
            }
            trySend(Result.failure(exception))
        }
        awaitClose { }
    }

}