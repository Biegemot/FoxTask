package com.foxtask.app

import android.app.Application
import com.foxtask.app.di.ServiceLocator
import com.foxtask.app.util.NotificationHelper

class FoxTaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        NotificationHelper.createNotificationChannel(this)
    }
}
