package com.chargealarm

import android.app.Application
import android.content.Context

class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // The following line triggers the initialization of ACRA
//        ACRA.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}
