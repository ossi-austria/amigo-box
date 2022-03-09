package org.ossiaustria.lib.domain.modules

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.ossiaustria.lib.domain.services.LoginCleanupService
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LibDomainKoinModulesTest : KoinTest {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @After
    @Before
    fun afterEach() {
        stopKoin()
    }

    @Test
    fun checkServiceModule() = checkModules {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        androidContext(context)
        declareMock<SettingsRepository>()
        declareMock<LoginCleanupService>()
        modules(
            dispatcherModule,
            apiModule,
            databaseModule,
            repositoryModule,
            serviceModule
        )
    }

    @Test
    fun checkRepositoryModule() = checkModules {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        androidContext(context)
        declareMock<SettingsRepository>()
        modules(
            dispatcherModule,
            apiModule,
            databaseModule,
            repositoryModule,
        )
    }

    @Test
    fun checkDatabaseModule() = checkModules {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        androidContext(context)
        modules(databaseModule)
    }

    @Test
    fun checkDispatcherModule() = checkModules {
        modules(dispatcherModule)
    }

    @Test
    fun checkApiModule() = checkModules {
        modules(apiModule)
    }
}