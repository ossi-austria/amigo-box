package org.ossiaustria.amigobox.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
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
            HomeFragmentComposable(
                viewModel.name,
                openNotifications = { navigator.toTimeline() },
                openAlbums = { navigator.toHome() },
                openContacts = { navigator.toContacts() })
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
    MaterialTheme {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {

            Text(
                stringResource(id = R.string.home_greeting_var, name),
                modifier = Modifier
                    .padding(start = 30.dp, bottom = 4.dp, top = 30.dp),
                style = MaterialTheme.typography.h2,
            )

                Text(
                    stringResource(id = R.string.home_subtitle),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .padding(start = 40.dp, bottom = 40.dp)
                        .height(50.dp)
                )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {

                CreateHomeButton(
                    R.drawable.contacts_icon,
                    stringResource(id = R.string.home_menu_open_contacts)
                ) {
                    openContacts()
                }

                CreateHomeButton(
                    R.drawable.albums_icon,
                    stringResource(R.string.Albums_button_description)
                ) {
                    openAlbums()
                }
                CreateHomeButton(
                    R.drawable.messages_icon,
                    stringResource(R.string.Messages_button_description)
                ) {
                    openNotifications()
                }
            }

        }
    }
}

@Preview
@Composable
fun PreviewHomeFragmentComposable() {
    HomeFragmentComposable("Emma", {}, {}, {})
}

@Composable
fun CreateHomeButton(
    iconId: Int,
    buttonDescription: String,
    onClick: () -> Unit
) {
    Card(
        border = BorderStroke(1.dp, androidx.compose.ui.graphics.Color.Black),
        modifier = Modifier
            .padding(24.dp)
            .clickable(onClick = onClick)
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            HomeBtnCard(iconId, buttonDescription)
        }
    }
}


