package org.ossiaustria.amigobox.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.commons.MaterialButton
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Person
import javax.inject.Inject

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    @Inject
    lateinit var navigator: Navigator

    private val loadingViewModel by viewModels<LoadingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { LoadingFragmentScreen(loadingViewModel) }
    }

    override fun onResume() {
        super.onResume()
        loadingViewModel.bind(navigator)
        loadingViewModel.loadAccount()
    }

    override fun onPause() {
        super.onPause()
        loadingViewModel.unbind()
    }
}

@Composable
fun LoadingFragmentScreen(loadingViewModel: LoadingViewModel = viewModel()) {
    val state: Resource<Boolean> by loadingViewModel.liveState.observeAsState(Resource.loading())
    val person: Person? by loadingViewModel.livePerson.observeAsState()
    LoadingFragmentComposable(
        state = state, person = person,
        startHome = loadingViewModel::startHome,
        startLogin = loadingViewModel::startLogin,
        startJitsi = loadingViewModel::startJitsi,
        startTimeline = loadingViewModel::startTimeline,
        startContacts = loadingViewModel::startContacts,
        startAlbums = loadingViewModel::startAlbums,
        startGallery = loadingViewModel::startGallery,
        startPersonDetail = loadingViewModel::startPersonDetail,
    )
}

@Composable
fun LoadingFragmentComposable(
    state: Resource<Boolean>,
    person: Person?,
    startHome: () -> Unit,
    startLogin: () -> Unit,
    startJitsi: () -> Unit,
    startTimeline: () -> Unit,
    startContacts: () -> Unit,
    startAlbums: () -> Unit,
    startGallery: () -> Unit,
    startPersonDetail: () -> Unit,
) {

    MaterialTheme {

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterEnd))
            }

        } else if (state.isSuccess) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Hallo ${person?.name}")
                MaterialButton(onClick = { startLogin() }, text = "startLogin")
                MaterialButton(onClick = { startHome() }, text = "Home")
                MaterialButton(onClick = { startJitsi() }, text = "Start jitsi")
                MaterialButton(onClick = { startTimeline() }, text = "Show Timeline")
                MaterialButton(onClick = { startContacts() }, text = "Show Contacts")
                MaterialButton(onClick = { startAlbums() }, text = "Show Albums")
                MaterialButton(onClick = { startGallery() }, text = "Show Gallery")
                MaterialButton(
                    onClick = { startPersonDetail() },
                    text = "Person detail (currentUser)"
                )
            }
        }
    }
}