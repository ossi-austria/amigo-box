package org.ossiaustria.digitaluser

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class DigitalUserApplication : Application() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}