package com.example.myapplication

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.control.HisController
import com.example.myapplication.data.control.database
import org.jetbrains.anko.notificationManager

class MyApp : Application() {

    override fun onCreate() {

        super.onCreate()

        c = DBController( applicationContext )
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        hist = this.database

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channel_id1, "My channel",
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "My channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)

            val channel2 = NotificationChannel(
                    channel_id2, "My channel2",
                    NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "My channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel2)
        }
    }

    companion object {

        const val channel_id1 = "id1"
        const val channel_id2 = "id2"

        lateinit var c: DBController
        lateinit var prefs: SharedPreferences
        lateinit var hist: HisController

    }

}