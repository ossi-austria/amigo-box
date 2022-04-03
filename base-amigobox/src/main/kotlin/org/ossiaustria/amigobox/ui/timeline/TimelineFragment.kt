package org.ossiaustria.amigobox.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.autoplay.TimerState
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight

class TimelineFragment : Fragment() {

    private val viewModel by viewModel<TimelineViewModel>()

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
                    val centerPerson = viewModel.centerPerson
                    val sendables by viewModel.sendables.observeAsState(emptyList())

                    if (centerPerson != null) {
                        TimelineContent(
                            currentIndex = currentIndex,
                            timerState = timerState,
                            centerPerson = centerPerson,
                            sendables = sendables,
                            findPerson = viewModel::findPerson,
                            toHome = navigator::toHome,
                            toAlbum = navigator::toImageGallery,
                            toCall = navigator::toCallFragment,
                            onPreviousPressed = viewModel::onPreviousPressed,
                            onNextPressed = viewModel::onNextPressed,
                            onStartStopPressed = viewModel::onStartStopPressed,
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadPersons()
        viewModel.loadAllSendables()
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
        viewModel.timerState.removeObservers(viewLifecycleOwner)
    }
}

