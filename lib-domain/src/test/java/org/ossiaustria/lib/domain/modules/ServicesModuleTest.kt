package org.ossiaustria.lib.domain.modules

import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules
import org.koin.test.inject
import org.koin.test.mock.MockProviderRule
import org.koin.test.mock.declareMock
import org.mockito.Mockito
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.ossiaustria.lib.domain.services.AlbumShareService
import org.ossiaustria.lib.domain.services.AuthService
import org.ossiaustria.lib.domain.services.CallService
import org.ossiaustria.lib.domain.services.MessageService
import org.ossiaustria.lib.domain.services.MultimediaService
import org.ossiaustria.lib.domain.services.TimelineService
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ServicesModuleTest : KoinTest {

    private val authService: AuthService by inject()
    private val messageService: MessageService by inject()
    private val albumShareService: AlbumShareService by inject()
    private val callService: CallService by inject()
    private val multimediaService: MultimediaService by inject()
    private val timelineService: TimelineService by inject()

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
    fun checkServiceModule() {
        checkModules {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            androidContext(context)
            declareMock<SettingsRepository>()
            modules(
                dispatcherModule,
                apiModule,
                databaseModule,
                repositoryModule,
                serviceModule
            )
            assertNotNull(authService)
            assertNotNull(messageService)
            assertNotNull(albumShareService)
            assertNotNull(callService)
            assertNotNull(multimediaService)
            assertNotNull(timelineService)
        }

    }
}