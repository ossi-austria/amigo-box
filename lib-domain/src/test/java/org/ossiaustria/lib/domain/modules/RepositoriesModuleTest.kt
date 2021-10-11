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
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.AlbumShareRepository
import org.ossiaustria.lib.domain.repositories.CallRepository
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.repositories.MessageRepository
import org.ossiaustria.lib.domain.repositories.MultimediaRepository
import org.ossiaustria.lib.domain.repositories.NfcTagRepository
import org.ossiaustria.lib.domain.repositories.PersonRepository
import org.ossiaustria.lib.domain.repositories.SettingsRepository
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoriesModuleTest : KoinTest {

    private val albumRepository: AlbumRepository by inject()
    private val albumShareRepository: AlbumShareRepository by inject()
    private val callRepository: CallRepository by inject()
    private val groupRepository: GroupRepository by inject()
    private val messageRepository: MessageRepository by inject()
    private val multimediaRepository: MultimediaRepository by inject()
    private val nfcTagRepository: NfcTagRepository by inject()
    private val personRepository: PersonRepository by inject()

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
    fun check() = checkModules {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        androidContext(context)
        declareMock<SettingsRepository>()
        modules(
            dispatcherModule,
            apiModule,
            databaseModule,
            repositoryModule,
        )
        assertNotNull(albumRepository)
        assertNotNull(albumShareRepository)
        assertNotNull(callRepository)
        assertNotNull(groupRepository)
        assertNotNull(messageRepository)
        assertNotNull(multimediaRepository)
        assertNotNull(nfcTagRepository)
        assertNotNull(personRepository)

    }
}