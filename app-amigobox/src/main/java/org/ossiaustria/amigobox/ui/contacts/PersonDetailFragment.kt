package org.ossiaustria.amigobox.ui.contacts

import ProfileImage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.HomeButtonsRow
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.amigobox.ui.commons.Toasts
import org.ossiaustria.lib.domain.models.Person

class PersonDetailFragment : Fragment() {

    val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        val person = Navigator.getPerson(requireArguments())
        setContent {
            if (person != null) {
                PersonDetailFragmentScreen(
                    person,
                    ::startCall, ::backToHome
                )
            } else {
                Text("No Person?")
                Toasts.personNotFound(requireContext())
            }
        }
    }

    private fun startCall() {
        val person = Navigator.getPerson(requireArguments())
        if (person != null) {
            navigator.toCallFragment(person)
        } else {
            Toasts.personNotFound(requireContext())
        }
    }

    private fun backToHome() {
        navigator.back()
    }
}

@Composable
fun PersonDetailFragmentScreen(
    person: Person,
    startCall: () -> Unit,
    toHome: () -> Unit,
) {
    AmigoThemeLight {
        Surface(color = MaterialTheme.colors.background) {
            PersonDetailFragmentComposable(
                person.name,
                person.absoluteAvatarUrl(),
                startCall,
                toHome
            )
        }
    }
}

@Composable
fun PersonDetailFragmentComposable(
    name: String,
    pictureUrl: String?,
    startCall: () -> Unit,
    toHome: () -> Unit,
) {

    Column(Modifier.fillMaxSize()) {
        HomeButtonsRow(onClickBack = toHome)
        Row(
            Modifier
                .padding(
                    start = UIConstants.PersonDetailFragment.SEC_ROW_PADDING_START,
                    end = UIConstants.PersonDetailFragment.SEC_ROW_PADDING,
                    top = UIConstants.PersonDetailFragment.SEC_ROW_PADDING,
                    bottom = UIConstants.PersonDetailFragment.SEC_ROW_PADDING
                )
        ) {

            ProfileImage(
                url = pictureUrl,
                contentScale = ContentScale.Crop,
                onClick = startCall
            )

            Column(modifier = Modifier.padding(UIConstants.PersonDetailFragment.COLUMN_PADDING)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.h2
                )
                TextAndIconButton(
                    iconId = R.drawable.ic_phone_call,
                    text = stringResource(R.string.person_detail_call_button, name),
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary,
                ) {
                    startCall()
                }
                TextAndIconButton(
                    iconId = R.drawable.ic_image_light,
                    text = stringResource(R.string.person_detail_albums_button, name),
                    backgroundColor = MaterialTheme.colors.secondary,
                    contentColor = MaterialTheme.colors.onSecondary,
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
