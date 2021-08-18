package com.android.aroundme

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoreApp : Application() {

    companion object {
          var mInstance: CoreApp? = null
    }

    @Synchronized
    fun getInstance(): CoreApp? {
        return mInstance
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

}
