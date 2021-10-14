package org.ossiaustria.amigobox.modules

import android.content.Context
import android.content.SharedPreferences
import io.mockk.mockkClass
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.ossiaustria.amigobox.ui.loading.SynchronisationService
import org.ossiaustria.lib.domain.modules.apiModule
import org.ossiaustria.lib.domain.modules.databaseModule
import org.ossiaustria.lib.domain.modules.dispatcherModule
import org.ossiaustria.lib.domain.modules.repositoryModule
import org.ossiaustria.lib.domain.modules.serviceModule
import org.ossiaustria.lib.domain.repositories.SettingsRepository

class AppServiceModuleTest : KoinTest {

    private val synchronisationService: SynchronisationService by inject()

    private val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        mockkClass(clazz)
    }

    @After
    @Before
    fun afterEach() {
        stopKoin()
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun check() = checkModules {
        declareMock<Context> { mockkClass(Context::class) }
        declareMock<SharedPreferences> { mockkClass(SharedPreferences::class) }
        declareMock<SettingsRepository> { mockkClass(SettingsRepository::class) }

        modules(
            dispatcherModule,
            apiModule,
            databaseModule,
            repositoryModule,
            serviceModule,
            appServiceModule
        )
        assertNotNull(synchronisationService)
    }
}