package com.michaelwoodroof.culinaryassistant.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.michaelwoodroof.culinaryassistant.R

object NotificationHandler {

    private const val CHANNEL_ID = "Culinary Assistant"

    fun createNotificationChannel(givenContext : Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = givenContext.getString(R.string.channel_name)
            val descriptionText = givenContext.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                givenContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    fun buildNotification(givenContext : Context, givenTitle : String, givenContent : String, givenPending : PendingIntent) : NotificationCompat.Builder? {

        return NotificationCompat.Builder(givenContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_circle_fill) // @TODO UPDATE TO REAL ICON
            .setContentTitle(givenTitle)
            .setContentText(givenContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set Intent that will launch on Notification
            .setContentIntent(givenPending)
            .setAutoCancel(true)
            .setTimeoutAfter(3600000) // Delete Notification after an Hour // @TODO REVIEW THIS

    }

    fun showNotification() {

    }

}