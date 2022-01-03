package org.ossiaustria.amigobox.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.ossiaustria.amigobox.nfc.NfcViewModel
import org.ossiaustria.amigobox.onboarding.OnboardingViewModel
import org.ossiaustria.amigobox.ui.albums.AlbumsViewModel
import org.ossiaustria.amigobox.ui.calls.CallViewModel
import org.ossiaustria.amigobox.ui.calls.IncomingEventsViewModel
import org.ossiaustria.amigobox.ui.contacts.ContactsViewModel
import org.ossiaustria.amigobox.ui.home.HomeViewModel
import org.ossiaustria.amigobox.ui.imagegallery.ImageGalleryViewModel
import org.ossiaustria.amigobox.ui.timeline.TimelineViewModel

val viewModelsModule = module {
    viewModel { AlbumsViewModel(get(), get()) }
    viewModel { OnboardingViewModel(get(), get(), get(), get()) }
    viewModel { TimelineViewModel(get(), get(), get(), get()) }
    viewModel { ContactsViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { ImageGalleryViewModel(get()) }
    viewModel { NfcViewModel(get(), get(), get(), get()) }
    viewModel { IncomingEventsViewModel(get(), get()) }
    viewModel { CallViewModel(get(), get(), get(), get()) }
}
