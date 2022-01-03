package org.ossiaustria.amigobox.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.HomeButtonsRow
import org.ossiaustria.amigobox.ui.commons.NavigationButtonType
import org.ossiaustria.amigobox.ui.commons.ScrollNavigationButton
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
import java.util.*

class ContactsFragment : Fragment() {

    val navigator: Navigator by inject()

    private val viewModel by viewModel<ContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { ContactsFragmentScreen(viewModel) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }
}

@Composable
fun ContactsFragmentScreen(viewModel: ContactsViewModel) {
    AmigoThemeLight {
        val persons by viewModel.persons.observeAsState(emptyList())
        Surface(color = MaterialTheme.colors.background) {
            ContactsFragmentContent(
                persons,
                viewModel::backToHome,
                viewModel::toContact
            )
        }
    }
}

@Composable
fun ContactsFragmentContent(
    persons: List<Person>,
    backToHome: () -> Unit,
    toContact: (Person) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(UIConstants.ListFragment.HEADER_PADDING_START)
    ) {
        HomeButtonsRow(onClickBack = backToHome)
        // Header
        Text(
            modifier = Modifier.padding(UIConstants.Defaults.INNER_PADDING),
            text = stringResource(R.string.contacts_headline),
            style = MaterialTheme.typography.h3
        )

        // Text Description
        Row(
            modifier = Modifier.padding(UIConstants.Defaults.INNER_PADDING),
        ) {
            Text(
                text = stringResource(R.string.contacts_usage_description),
                style = MaterialTheme.typography.body1
            )
        }

        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()

        Row(
            modifier = Modifier
                .padding(UIConstants.Defaults.INNER_PADDING)
                .horizontalScroll(scrollState)

        ) {

            persons.forEach { person ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(UIConstants.ScrollableCardList.CARD_PADDING)
                        .clickable { toContact(person) },
                    elevation = UIConstants.ScrollableCardList.CARD_ELEVATION,
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.background(MaterialTheme.colors.background)
                    ) {
                        LoadPersonCardContent(person.name, person.absoluteAvatarUrl())
                    }
                }
            }
        }

        // Back and Forward Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConstants.Defaults.INNER_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            // Backwards
            ScrollNavigationButton(
                type = NavigationButtonType.PREVIOUS,
                text = stringResource(R.string.previous_scroll_navigation_btn),
                scrollState = scrollState,
            ) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.value - UIConstants.ScrollButton.SCROLL_DISTANCE)
                }
            }

            // Forwards
            ScrollNavigationButton(
                type = NavigationButtonType.NEXT,
                text = stringResource(R.string.next_scroll_navigation_btn),
                scrollState = scrollState
            ) {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.value + UIConstants.ScrollButton.SCROLL_DISTANCE)
                }
            }
        }
    }

}

@Preview
@Composable
fun ContactsFragmentContentPreview() {
    ContactsFragmentContent(
        ContactsSourceMockData.listOfPeopleWithImages(),
        { },
        { },
    )
}

private object ContactsSourceMockData {

    private val groupId = UUID.randomUUID()

    val lukas = createPersonMock("Lukas", "lukas@tum.de")

    fun listOfPeopleWithImages(): MutableList<Person> {
        return mutableListOf(
            lukas,
            createPersonMock("Michl", "michl@tum.de"),
            createPersonMock("Moni", "moni@tum.de"),
            createPersonMock("Peter", "peter@tum.de"),
            createPersonMock("Flo", "flo@tum.de"),
        )
    }

    fun createPersonMock(name: String, email: String) =
        Person(
            UUID.randomUUID(),
            name,
            groupId,
            MemberType.MEMBER,
            "https://thispersondoesnotexist.com/image",
            email
        )

}