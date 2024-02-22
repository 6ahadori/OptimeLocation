package com.bahadori.optimeassignment.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.bahadori.optimeassignment.R
import com.bahadori.optimeassignment.core.data.service.LocationService
import com.bahadori.optimeassignment.core.data.service.LocationService.Companion.ACTION_STOP_SELF
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private lateinit var notificationManager: NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    fun showNotification(title: String, content: String, silent: Boolean = true) {
        notificationManager.notify(
            ONGOING_NOTIFICATION_ID,
            buildNotification(title, content, silent)
        )
    }

    fun startForeground(service: Service) {
        val notification = buildNotification("Observing Location", "Connection State")
        service.startForeground(ONGOING_NOTIFICATION_ID, notification)
    }

    private fun buildNotification(title: String, content: String, silent: Boolean = true) =
        NotificationCompat.Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setSilent(silent)
            .setSmallIcon(R.drawable.ic_location)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(title)
            .setContentText(content)
            .addAction(
                0,
                "STOP",
                PendingIntent.getService(
                    context,
                    0,
                    Intent(context, LocationService::class.java).apply {
                        action = ACTION_STOP_SELF
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()

    companion object {
        private const val CHANNEL_ID = "OptimeLocationID"
        private const val CHANNEL_NAME = "OptimeLocation"
        private const val ONGOING_NOTIFICATION_ID = 1376
    }
}