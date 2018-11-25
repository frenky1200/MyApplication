package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.control.DBController

class MyApp : Application() {

    override fun onCreate() {

        super.onCreate()

        instance = this
        c = DBController( applicationContext )

    }

    companion object {
        lateinit var c: DBController

        var instance: MyApp? = null
            private set
    }

}