package org.ossiaustria.amigobox.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.onboarding.OnboardingViewModel
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton

class HomeFragment : Fragment() {

    private val viewModel by viewModel<HomeViewModel>()
    private val onboardingViewModel by viewModel<OnboardingViewModel>()

    val navigator: Navigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            AmigoThemeLight {
                Surface(color = MaterialTheme.colors.background) {
                    HomeFragmentComposable(
                        viewModel.name,
                        openNotifications = { navigator.toTimeline() },
                        openAlbums = { navigator.toAlbums() },
                        openContacts = { navigator.toContacts() },
                        logout = { logout() },
                    )
                }
            }
        }
    }

    private fun logout() {
        onboardingViewModel.logout {
            navigator.toLoading()
        }
    }
}

@Composable
fun HomeFragmentComposable(
    name: String,
    openNotifications: () -> Unit,
    openAlbums: () -> Unit,
    openContacts: () -> Unit,
    logout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            stringResource(id = R.string.home_greeting_var, name),
            modifier = Modifier.padding(
                start = UIConstants.HomeFragment.PADDING_START,
                top = UIConstants.HomeFragment.HEADER_PADDING_TOP
            ),
            style = MaterialTheme.typography.h1
        )

        Text(
            stringResource(id = R.string.home_subtitle),
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(
                start = UIConstants.HomeFragment.PADDING_START,
                top = UIConstants.HomeFragment.DESCRIPTION_PADDING_TOP,
                bottom = UIConstants.HomeFragment.DESCRIPTION_PADDING_BOTTOM
            ),
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()

        ) {
            CreateHomeButton(
                R.drawable.ic_user_light,
                stringResource(id = R.string.home_menu_open_contacts),
                false
            ) { openContacts() }
            CreateHomeButton(
                R.drawable.ic_image_light,
                stringResource(R.string.Albums_button_description),
                false
            ) { openAlbums() }
            CreateHomeButton(
                R.drawable.ic_chat_light,
                stringResource(R.string.Messages_button_description),
                true
            ) { openNotifications() }
        }
        Spacer(modifier = Modifier.weight(1.0f)) // fill height with spacer
        TextAndIconButton(
            iconId = null,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End),
            onClick = { logout() },
            text = stringResource(R.string.home_logout)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeFragmentComposable() {
    AmigoThemeLight {
        HomeFragmentComposable("Emma", {}, {}, {}, {})
    }
}

@Composable
fun CreateHomeButton(
    iconId: Int,
    buttonDescription: String,
    showCounter: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(UIConstants.HomeButtonsCard.CARD_PADDING)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(UIConstants.HomeButtonsCard.CARD_SHAPE)
    ) {
        HomeBtnCard(iconId, buttonDescription, showCounter)
    }
}


