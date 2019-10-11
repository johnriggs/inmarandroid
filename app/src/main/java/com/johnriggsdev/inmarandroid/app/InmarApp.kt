package com.johnriggsdev.inmarandroid.app

import android.app.Application
import android.content.Context

class InmarApp : Application() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: InmarApp private set

        fun applicationContext() : Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = InmarApp.applicationContext()
    }
}