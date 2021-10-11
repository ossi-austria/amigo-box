package org.ossiaustria.amigobox.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ossiaustria.amigobox.onboarding.OnboardingViewModel
import org.ossiaustria.amigobox.timeline.TimelineViewModel
import org.ossiaustria.amigobox.ui.albums.AlbumsViewModel
import org.ossiaustria.amigobox.ui.contacts.ContactsViewModel
import org.ossiaustria.amigobox.ui.home.HomeViewModel
import org.ossiaustria.amigobox.ui.imagegallery.ImageGalleryViewModel
import org.ossiaustria.amigobox.ui.loading.LoadingViewModel

val viewModelsModule = module {
    viewModel { AlbumsViewModel() }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { TimelineViewModel(get(), get()) }
    viewModel { ContactsViewModel() }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ImageGalleryViewModel(get()) }
    viewModel { LoadingViewModel(get(), get(), get()) }
}
