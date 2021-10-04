package org.ossiaustria.amigobox.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.MaterialThemeLight
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var navigator: Navigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MaterialThemeLight {
                Surface(color = MaterialTheme.colors.background) {
                    HomeFragmentComposable(
                        viewModel.name,
                        openNotifications = { navigator.toTimeline() },
                        openAlbums = { navigator.toAlbums() },
                        openContacts = { navigator.toContacts() },
                    )
                }
            }
        }
    }
}

@Composable
fun HomeFragmentComposable(
    name: String,
    openNotifications: () -> Unit,
    openAlbums: () -> Unit,
    openContacts: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
    {

        Text(
            stringResource(id = R.string.home_greeting_var, name),
            modifier = Modifier
                .padding(
                    start = UIConstants.HomeFragment.PADDING_START,
                    top = UIConstants.HomeFragment.HEADER_PADDING_TOP
                ),
            style = MaterialTheme.typography.h1
        )

        Text(
            stringResource(id = R.string.home_subtitle),
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(
                    start = UIConstants.HomeFragment.PADDING_START,
                    top = UIConstants.HomeFragment.DESCRIPTION_PADDING_TOP,
                    bottom = UIConstants.HomeFragment.DESCRIPTION_PADDING_BOTTOM
                ),

            )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()

        ) {

            CreateHomeButton(
                R.drawable.ic_user_light,
                stringResource(id = R.string.home_menu_open_contacts),
                false
            ) {
                openContacts()
            }
            CreateHomeButton(
                R.drawable.ic_image_light,
                stringResource(R.string.Albums_button_description),
                false
            ) {
                openAlbums()
            }
            CreateHomeButton(
                R.drawable.ic_chat_light,
                stringResource(R.string.Messages_button_description),
                true
            ) {
                openNotifications()
            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHomeFragmentComposable() {
    MaterialThemeLight {
        HomeFragmentComposable("Emma", {}, {}, {})
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
    )
    {
        HomeBtnCard(iconId, buttonDescription, showCounter)

    }
}


