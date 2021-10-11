package org.ossiaustria.lib.nfc.demoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

class NfcDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}