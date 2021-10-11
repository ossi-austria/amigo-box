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
import org.ossiaustria.amigobox.onboarding.OnboardingViewModel
import org.ossiaustria.amigobox.timeline.TimelineViewModel
import org.ossiaustria.amigobox.ui.albums.AlbumsViewModel
import org.ossiaustria.amigobox.ui.contacts.ContactsViewModel
import org.ossiaustria.amigobox.ui.home.HomeViewModel
import org.ossiaustria.amigobox.ui.imagegallery.ImageGalleryViewModel
import org.ossiaustria.amigobox.ui.loading.LoadingViewModel
import org.ossiaustria.lib.domain.modules.apiModule
import org.ossiaustria.lib.domain.modules.databaseModule
import org.ossiaustria.lib.domain.modules.dispatcherModule
import org.ossiaustria.lib.domain.modules.repositoryModule
import org.ossiaustria.lib.domain.modules.serviceModule
import org.ossiaustria.lib.domain.repositories.SettingsRepository

class AppViewModelModuleTest : KoinTest {

    private val AlbumsViewModel: AlbumsViewModel by inject()
    private val OnboardingViewModel: OnboardingViewModel by inject()
    private val TimelineViewModel: TimelineViewModel by inject()
    private val ContactsViewModel: ContactsViewModel by inject()
    private val HomeViewModel: HomeViewModel by inject()
    private val ImageGalleryViewModel: ImageGalleryViewModel by inject()
    private val LoadingViewModel: LoadingViewModel by inject()

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
            appServiceModule,
            viewModelsModule
        )
        assertNotNull(AlbumsViewModel)
        assertNotNull(OnboardingViewModel)
        assertNotNull(TimelineViewModel)
        assertNotNull(ContactsViewModel)
        assertNotNull(HomeViewModel)
        assertNotNull(ImageGalleryViewModel)
        assertNotNull(LoadingViewModel)
    }
}