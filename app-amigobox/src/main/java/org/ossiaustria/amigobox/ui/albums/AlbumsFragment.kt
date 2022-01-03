package org.ossiaustria.amigobox.ui.albums

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
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight

class AlbumsFragment : Fragment() {

    val navigator: Navigator by inject()

    private val viewModel by viewModel<AlbumsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AmigoThemeLight {
                val albums by viewModel.albums.observeAsState(emptyList())
                Surface(color = MaterialTheme.colors.background) {
                    AlbumsFragmentComposable(
                        albums,
                        viewModel::backToHome,
                        viewModel::toAlbum,
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }
}
