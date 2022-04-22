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
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.albums.AlbumsViewModel
import org.ossiaustria.amigobox.ui.contacts.ContactsViewModel
import org.ossiaustria.amigobox.ui.home.HomeViewModel
import org.ossiaustria.amigobox.ui.imagegallery.ImageGalleryViewModel
import org.ossiaustria.amigobox.ui.loading.OnboardingViewModel
import org.ossiaustria.amigobox.ui.timeline.TimelineViewModel
import org.ossiaustria.lib.domain.modules.apiModule
import org.ossiaustria.lib.domain.modules.databaseModule
import org.ossiaustria.lib.domain.modules.dispatcherModule
import org.ossiaustria.lib.domain.modules.repositoryModule
import org.ossiaustria.lib.domain.modules.serviceModule
import org.ossiaustria.lib.domain.repositories.SettingsRepository

class AppViewModelModuleTest : KoinTest {

    private val albumsViewModel: AlbumsViewModel by inject()
    private val onboardingViewModel: OnboardingViewModel by inject()
    private val timelineViewModel: TimelineViewModel by inject()
    private val contactsViewModel: ContactsViewModel by inject()
    private val homeViewModel: HomeViewModel by inject()
    private val galleryViewModel: ImageGalleryViewModel by inject()

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
        declareMock<Navigator> { mockkClass(Navigator::class) }

        modules(
            dispatcherModule,
            apiModule,
            databaseModule,
            repositoryModule,
            serviceModule,
            appServiceModule,
            viewModelsModule
        )
        assertNotNull(albumsViewModel)
        assertNotNull(onboardingViewModel)
        assertNotNull(timelineViewModel)
        assertNotNull(contactsViewModel)
        assertNotNull(homeViewModel)
        assertNotNull(galleryViewModel)
    }
}