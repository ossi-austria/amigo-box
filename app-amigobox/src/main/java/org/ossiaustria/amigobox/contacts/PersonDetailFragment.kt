package org.ossiaustria.amigobox.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.AmigoStyle
import org.ossiaustria.amigobox.ui.commons.IconButtonSmall
import org.ossiaustria.amigobox.ui.commons.MaterialThemeLight
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.amigobox.ui.commons.profileImage
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
        setContent { PersonDetailScreen(globalState, ::startCall, ::toHome) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val person = Navigator.getPerson(requireArguments())
        globalState.setCurrentPerson(person)
        // init stuff
    }

    fun startCall() {
        navigator.toCallPerson(globalState.selectedPerson.value!!)
    }

    fun toHome() {
        navigator.toHome()
    }
}

@Composable
fun PersonDetailScreen(
    state: GlobalStateViewModel,
    startCall: () -> Unit,
    toHome: () -> Unit,
) {
    val personState by state.selectedPerson.observeAsState()
    if (personState != null) {
        MaterialThemeLight {
            Surface(color = MaterialTheme.colors.background) {
                PersonDetailFragmentComposable(
                    personState!!.name,
                    "https://picsum.photos/300/300",
                    startCall,
                    toHome
                )
            }
        }
    }
}

@Composable
fun PersonDetailFragmentComposable(
    name: String,
    pictureUrl: String,
    startCall: () -> Unit,
    toHome: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .padding(UIConstants.PersonDetailFragment.TOP_BAR_PADDING)
                .fillMaxWidth()
                .height(UIConstants.PersonDetailFragment.TOP_BAR_HEIGHT),
            horizontalArrangement = Arrangement.End
        ) {
            IconButtonSmall(
                resourceId = R.drawable.ic_home_icon,
                backgroundColor = MaterialTheme.colors.background,
                fillColor = MaterialTheme.colors.surface,
            ) {
                toHome()
            }
            IconButtonSmall(
                resourceId = R.drawable.ic_help_icon,
                backgroundColor = MaterialTheme.colors.background,
                fillColor = MaterialTheme.colors.primary,
            ) {
                //TODO: Add help screens
            }
        }
        Row(
            Modifier
                .padding(UIConstants.PersonDetailFragment.SEC_ROW_PADDING)
        ) {
            /*NetworkImage(
                url = pictureUrl,
                modifier = Modifier
                    .fillMaxWidth(0.5F),
                contentScale = ContentScale.Crop
            )
             */
            profileImage(
                url = pictureUrl,
                modifier = Modifier,
                contentScale = ContentScale.Crop,
            )

            Column(modifier = Modifier.padding(UIConstants.PersonDetailFragment.COLUMN_PADDING)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.h2
                )
                Spacer(modifier = Modifier.padding(AmigoStyle.Dim.C))
                TextAndIconButton(
                    R.drawable.ic_phone_call,
                    stringResource(R.string.person_detail_call_button, name),
                    MaterialTheme.colors.secondary,
                    MaterialTheme.colors.onSecondary,
                    false,
                    true,
                    UIConstants.PersonDetailFragment.CALL_BUTTON_WIDTH
                ) { startCall() }
                Spacer(modifier = Modifier.padding(AmigoStyle.Dim.C))
                TextAndIconButton(
                    R.drawable.ic_image_light,
                    stringResource(R.string.person_detail_albums_button, name),
                    MaterialTheme.colors.secondary,
                    MaterialTheme.colors.onSecondary,
                    false,
                    true,
                    UIConstants.PersonDetailFragment.ALBUM_BUTTON_WIDTH
                ) {
                    //TODO: missing fragment
                }
            }
        }
    }
}

@Preview
@Composable
fun PersonDetailFragmentPreview() {
    PersonDetailFragmentComposable(
        "name", "pictureUrl", {}, {}
    )
}
