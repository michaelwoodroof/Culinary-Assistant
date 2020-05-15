package com.michaelwoodroof.culinaryassistant.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

class ScheduleNotification { //@TODO ADD CHECK TO SEE IF TIME HAS ALREADY PASSED

    fun createAlarm(givenContext : Context, givenHour : Int, givenMinute : Int, givenInterval : Long, givenMessage : String) {

        // Check Current Time

        var alarmMgr : AlarmManager? = null

        alarmMgr = givenContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(givenContext, NotificationReceiver::class.java)
        intent.putExtra("Message", givenMessage)

        val alarmIntent = PendingIntent.getBroadcast(givenContext, 0, intent, 0)

        val calendar : Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, givenHour)
            set(Calendar.MINUTE, givenMinute)
            set(Calendar.SECOND, 1)
        }

        alarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            givenInterval,
            alarmIntent
        )

    }

}