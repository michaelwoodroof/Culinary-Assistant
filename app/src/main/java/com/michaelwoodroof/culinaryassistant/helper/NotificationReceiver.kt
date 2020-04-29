package com.michaelwoodroof.culinaryassistant.helper

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.michaelwoodroof.culinaryassistant.mealPlanner.MealPlanner

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(givenContext: Context, givenIntent: Intent) {
        // Handle Notification
        val notificationId = 111
        val ni : Intent = Intent(givenContext, MealPlanner::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pi : PendingIntent = PendingIntent.getActivity(givenContext, 0, ni, 0)
        NotificationHandler.buildNotification(givenContext, "Test", "Desc", pi)

        val builder = NotificationHandler.buildNotification(givenContext, "Meal Planner", givenIntent.getStringExtra("Message"), pi)

        // Add Broadcasts for the Needed Notification Times
        with(NotificationManagerCompat.from(givenContext)) {
            notify(notificationId, builder!!.build())
        }

        Log.d("testData", "Broadcast")
    }

}