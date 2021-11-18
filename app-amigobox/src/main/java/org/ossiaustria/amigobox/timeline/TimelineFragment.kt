package org.ossiaustria.amigobox.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.autoplay.AutoState
import org.ossiaustria.amigobox.ui.autoplay.AutoplayCommons
import org.ossiaustria.amigobox.ui.autoplay.CountdownFormat
import org.ossiaustria.amigobox.ui.autoplay.GalleryNavState
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.imagegallery.formatTime
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.Sendable
import java.util.*
import kotlin.time.ExperimentalTime

class TimelineFragment : Fragment() {

    // Retrieve OnboardingViewModel via injection
    private val viewModel by viewModel<TimelineViewModel>()

    val navigator: Navigator by inject()

    @ExperimentalTime
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            TimelineScreen(
                viewModel,
                navigator::toHome,
                navigator::toImageGallery,
                navigator::toCallFragment
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadAllSendables()
        viewModel.loadPersons()
        // init stuff
        viewModel.initTimer()
    }
}

@ExperimentalTime
@Composable
fun TimelineScreen(
    viewModel: TimelineViewModel,
    toHome: () -> Unit,
    toAlbum: (Album) -> Unit,
    toCall: (Person) -> Unit,
) {

    AmigoThemeLight {
        Surface(color = MaterialTheme.colors.secondary) {

            val autoplay = AutoplayCommons()

            val navigationState by viewModel.navigationState.observeAsState(GalleryNavState.PLAY)
            val currentIndex by viewModel.currentGalleryIndex.observeAsState()
            val time by viewModel.time.observeAsState(CountdownFormat.TIME_COUNTDOWN.formatTime())
            val autoState by viewModel.autoState.observeAsState()
            val centerPerson = viewModel.centerPerson
            val sendables by viewModel.sendables.observeAsState(emptyList())

            if (centerPerson != null) {
                TimelineContent(
                    toHome,
                    toAlbum,
                    sendables,
                    viewModel::cancelTimer,
                    currentIndex,
                    viewModel::setGalleryIndex,
                    viewModel::setAutoState,
                    time,
                    autoState,
                    viewModel::startTimer,
                    navigationState,
                    viewModel::setNavigationState,
                    viewModel::pauseTimer,
                    centerPerson,
                    toCall,
                    viewModel::findPerson,
                    autoplay
                )
            }
        }
    }
}

@ExperimentalTime
@Composable
fun TimelineContent(
    toHome: () -> Unit,
    toAlbum: (Album) -> Unit,
    sendables: List<Sendable>,
    cancelTimer: () -> Unit,
    currentIndex: Int?,
    setGalleryIndex: (Int) -> Unit,
    setAutoState: (AutoState) -> Unit,
    time: String,
    autoState: AutoState?,
    startTimer: () -> Unit,
    navigationState: GalleryNavState?,
    setNavigationState: (GalleryNavState) -> Unit,
    pauseTimer: () -> Unit,
    centerPerson: Person,
    toCall: (Person) -> Unit,
    findPerson: (UUID) -> Person?,
    autoplay: AutoplayCommons
) {
    Box {
        InnerDynamicBox(
            sendables,
            toAlbum,
            toHome,
            cancelTimer,
            currentIndex,
            setGalleryIndex,
            setAutoState,
            time,
            autoState,
            centerPerson,
            toCall,
            findPerson,
            autoplay
        )
    }
    Box {
        OuterStaticBox(
            toHome,
            cancelTimer,
            setGalleryIndex,
            startTimer,
            currentIndex,
            navigationState,
            setNavigationState,
            pauseTimer,
            sendables,
            centerPerson,
            findPerson,
            autoplay
        )
    }
}

