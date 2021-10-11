package org.ossiaustria.amigobox.modules

import android.content.Context
import android.content.SharedPreferences
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
import org.mockito.Mockito
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.cloudmessaging.FCMHelper
import org.ossiaustria.lib.domain.modules.apiModule
import org.ossiaustria.lib.domain.modules.databaseModule
import org.ossiaustria.lib.domain.modules.dispatcherModule
import org.ossiaustria.lib.domain.modules.repositoryModule
import org.ossiaustria.lib.domain.modules.serviceModule
import org.ossiaustria.lib.domain.repositories.SettingsRepository

class AppModuleTest : KoinTest {

    private val cloudPushHandlerService: CloudPushHandlerService by inject()
    private val fcmHelper: FCMHelper by inject()
    private val navigator: Navigator by inject()

    private val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @After
    @Before
    fun afterEach() {
        stopKoin()
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun check() = checkModules {
        declareMock<Context> { Mockito.mock(Context::class.java) }
        declareMock<SharedPreferences> { Mockito.mock(SharedPreferences::class.java) }
        declareMock<SettingsRepository> { Mockito.mock(SettingsRepository::class.java) }

        modules(
            dispatcherModule,
            apiModule,
            databaseModule,
            repositoryModule,
            serviceModule,
            appModule
        )
        assertNotNull(cloudPushHandlerService)
        assertNotNull(fcmHelper)
        assertNotNull(navigator)
    }
}