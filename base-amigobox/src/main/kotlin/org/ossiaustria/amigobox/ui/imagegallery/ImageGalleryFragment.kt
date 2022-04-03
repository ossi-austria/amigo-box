package org.ossiaustria.amigobox.ui.imagegallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.autoplay.TimerState
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.LoadingWidget

class ImageGalleryFragment : Fragment() {

    private val viewModel by viewModel<ImageGalleryViewModel>()

    val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {

        setContent {
            AmigoThemeLight {
                Surface(color = MaterialTheme.colors.secondary) {

                    val timerState by viewModel.timerState.observeAsState(TimerState.STOP)
                    val currentIndex by viewModel.currentIndex.observeAsState(0)
                    val album by viewModel.album.observeAsState()

                    if (album != null) {
                        GalleryFragmentComposable(
                            currentIndex = currentIndex,
                            timerState = timerState,
                            items = album!!.itemsWithMedia,
                            toHome = navigator::back,
                            onPreviousPressed = viewModel::onPreviousPressed,
                            onNextPressed = viewModel::onNextPressed,
                            onStartStopPressed = viewModel::onStartStopPressed,
                        )
                    } else {
                        LoadingWidget()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val album = Navigator.getAlbum(requireArguments())
        viewModel.prepare(album)
    }

    override fun onResume() {
        super.onResume()
        viewModel.timerState.observe(viewLifecycleOwner) { timerState ->
            if (timerState == TimerState.FINISHED) {
                navigator.toHome()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
        viewModel.timerState.removeObservers(viewLifecycleOwner)
    }
}