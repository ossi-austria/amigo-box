package org.ossiaustria.amigobox.ui.imagegallery

import org.ossiaustria.lib.domain.models.Album
import timber.log.Timber

class ImageGalleryHelper {
    fun playButtonText(galleryNavState: GalleryNavState?): String {
        when (galleryNavState) {
            GalleryNavState.PLAY -> return "Diashow anhalten"
            else -> return "Diashow starten"
        }
    }

    fun nextPressed(
        viewModel: ImageGalleryViewModel,
        currentIndex: Int?,
        album: Album,
        navigationState: GalleryNavState?
    ) {
        Timber.w("next pressed!!")
        if (currentIndex != null && currentIndex < album.items.size) {
            Timber.w("Ablbum size: " + album.items.size.toString())
            viewModel.cancelTimer()
            viewModel.setGalleryIndex(currentIndex + 1)
            if (navigationState == GalleryNavState.PLAY) {
                viewModel.startTimer()
            }
        }

    }

    fun startStopPressed(viewModel: ImageGalleryViewModel, galleryNavState: GalleryNavState?) {
        if (galleryNavState == GalleryNavState.STOP) {
            viewModel.startTimer()
            viewModel.setNavigationState(GalleryNavState.PLAY)
        } else {
            viewModel.pauseTimer()
            viewModel.setNavigationState(GalleryNavState.STOP)
        }
    }

    fun previousPressed(
        viewModel: ImageGalleryViewModel,
        currentIndex: Int?,
        navigationState: GalleryNavState?
    ) {
        if (currentIndex != null && currentIndex > 0) {
            viewModel.cancelTimer()
            viewModel.setGalleryIndex(currentIndex - 1)
            if (navigationState == GalleryNavState.PLAY) {
                viewModel.startTimer()
            }
        }
    }

    fun initTimer(viewModel: ImageGalleryViewModel) {
        viewModel.cancelTimer()
        viewModel.startTimer()
    }

    fun handleImages(
        viewModel: ImageGalleryViewModel,
        time: String,
        currentIndex: Int?,
        autoState: AutoState?
    ) {
        Timber.w("setting Index")
        if ((time == "00:00") and (autoState == AutoState.CHANGE)) {
            if (currentIndex != null) {
                viewModel.setGalleryIndex(currentIndex + 1)
                viewModel.setAutoState(AutoState.CHANGED)
            }
        } else if ((time != "00:00") and (autoState != AutoState.CHANGE)) {
            viewModel.setAutoState(AutoState.CHANGE)
        }
    }
}