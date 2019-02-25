package com.example.myapplication

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.control.HisController
import com.example.myapplication.data.control.database
import ru.profit_group.scorocode_sdk.ScorocodeSdk

class MyApp : Application() {

    override fun onCreate() {

        super.onCreate()

        ScorocodeSdk.initWith(APPLICATION_ID, CLIENT_KEY, null, FILE_KEY, MESSAGE_KEY, SCRIPT_KEY, null)
        instance = this
        c = DBController( applicationContext )
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        hist = this.database
    }

    companion object {

        const val APPLICATION_ID = "fd10b71b4a1e447380414a66272218f7"
        const val CLIENT_KEY = "8c16b9b1162f48558f6ac9f163c17370"
        const val FILE_KEY = "063eb7b046ae4400bf380d6bde6287c6"
        private const val MESSAGE_KEY = "56a4e6e802624f528beadedf3b2191f9"
        private const val SCRIPT_KEY = "8792445a8f8f43f59cca2fe9f5c99f81"

        lateinit var c: DBController
        lateinit var prefs: SharedPreferences
        lateinit var hist: HisController

        var instance: MyApp? = null
            private set
    }

}