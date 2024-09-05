package org.example.project

import android.app.Application

class AppApplication : Application() {

    companion object {
        lateinit var instance:AppApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@AppApplication
    }
}