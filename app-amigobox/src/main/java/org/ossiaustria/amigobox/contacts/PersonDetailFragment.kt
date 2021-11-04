package org.ossiaustria.amigobox.contacts

import ProfileImage
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
import org.ossiaustria.amigobox.ui.commons.AmigoStyle
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.IconButtonSmall
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
        Row(
            Modifier
                .padding(
                    top = UIConstants.HomeButtonRow.TOP_PADDING,
                    end = UIConstants.HomeButtonRow.END_PADDING
                )
                .fillMaxWidth()
                .height(UIConstants.HomeButtonRow.HEIGHT),
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
                Spacer(modifier = Modifier.padding(AmigoStyle.Dim.C))
                TextAndIconButton(
                    R.drawable.ic_phone_call,
                    stringResource(R.string.person_detail_call_button, name),
                    MaterialTheme.colors.secondary,
                    MaterialTheme.colors.onSecondary,
                    false,
                    true,
                    UIConstants.PersonDetailFragment.CALL_BUTTON_WIDTH
                ) {
                    startCall()
                }
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
