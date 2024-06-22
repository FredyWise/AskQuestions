package com.fredy.askquestions

import android.app.Application
import androidx.core.app.NotificationManagerCompat
//import com.fredy.mysavings.DI.AppModule
//import com.fredy.mysavings.DI.AppModuleImpl
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BearChatApp: Application(){

//    @Inject
//    lateinit var notificationManager: NotificationManagerCompat
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

//        notificationManager
    }
}