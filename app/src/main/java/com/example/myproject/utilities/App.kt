package com.example.myproject.utilities

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        SignalManager.init(this)
        VibrationManager.init(this)
    }
}