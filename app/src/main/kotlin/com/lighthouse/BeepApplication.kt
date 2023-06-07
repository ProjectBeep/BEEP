package com.lighthouse

import android.app.Application
import com.lighthouse.utils.log.ComponentLogger
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BeepApplication : Application() {

    @Inject
    lateinit var componentLogger: ComponentLogger

//    @Inject
//    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        componentLogger.initialize(this)
    }

//    override fun getWorkManagerConfiguration() =
//        Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
}
