package com.example.niumo

import android.app.Application

class App : Application() {
    //全局变量token
    companion object {
        lateinit var INSTANCE: App
            private set
        var token: String? = null
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}