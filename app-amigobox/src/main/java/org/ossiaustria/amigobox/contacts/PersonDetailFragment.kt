package org.ossiaustria.amigobox.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.commons.AmigoStyle
import org.ossiaustria.amigobox.ui.commons.MaterialButton
import org.ossiaustria.amigobox.ui.commons.NetworkImage
import javax.inject.Inject

@AndroidEntryPoint
class PersonDetailFragment : Fragment() {

    private val globalState: GlobalStateViewModel by activityViewModels()

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { PersonDetailScreen(globalState) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val person = Navigator.getPerson(requireArguments())
        globalState.setCurrentPerson(person)
        // init stuff
    }

    @Composable
    fun PersonDetailScreen(state: GlobalStateViewModel) {
        val personState by state.selectedPerson.observeAsState()
        if (personState != null) {
            MaterialTheme {
                PersonDetailFragmentComposable(personState!!.name, "https://picsum.photos/300/300")
            }
        }
    }

    @Composable
    fun PersonDetailFragmentComposable(name: String, pictureUrl: String) {
        MaterialTheme {
            Row(Modifier.padding(16.dp)) {
                NetworkImage(
                    url = pictureUrl,
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.h2
                    )
                    Spacer(modifier = Modifier.padding(AmigoStyle.Dim.C))
                    MaterialButton(
                        onClick = { startCall() },
                        modifier = Modifier,
                        text = stringResource(R.string.person_detail_call_button, name)
                    )
                    Spacer(modifier = Modifier.padding(AmigoStyle.Dim.C))
                    MaterialButton(
                        onClick = { startCall() },
                        modifier = Modifier,
                        text = stringResource(R.string.person_detail_albums_button, name)
                    )
                }
            }
        }
    }

    private fun startCall() {
        navigator.toCallPerson(globalState.selectedPerson.value!!)
    }

}
