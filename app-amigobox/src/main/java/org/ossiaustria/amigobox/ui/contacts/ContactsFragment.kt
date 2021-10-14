package org.ossiaustria.amigobox.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.NavigationButton
import org.ossiaustria.amigobox.ui.commons.ScrollButtonType
import org.ossiaustria.amigobox.ui.commons.ScrollNavigationButton
import org.ossiaustria.lib.domain.models.Person

class ContactsFragment : Fragment() {

    val navigator: Navigator by inject()

    private val viewModel by viewModel<ContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { ContactsScreen(viewModel) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.load()
    }

    @Composable
    fun ContactsScreen(viewModel: ContactsViewModel) {
        MaterialTheme {
            val persons by viewModel.persons.observeAsState(emptyList())
            ContactsFragmentComposable(persons)
        }
    }

    @Preview
    @Composable
    fun ContactsFragmentComposablePreview() {
        val listOfPeopleWithImages = ContactsSourceMockData.listOfPeopleWithImages()
        ContactsFragmentComposable(listOfPeopleWithImages)
    }

    @Composable
    fun ContactsFragmentComposable(persons: List<Person>) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, end = 16.dp)
                    .height(40.dp),
                horizontalArrangement = Arrangement.End
            ) {
                // Home Button
                NavigationButton(onClick = { backToHome() }, text = "Zurück zum Start")
            }
            // Text "Kontaktliste"
            Row(
                modifier = Modifier
                    .padding(start = 40.dp, top = 4.dp, bottom = 4.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Kontaktliste",
                    fontSize = 40.sp
                )
            }

            // Text Description
            Row(
                modifier = Modifier
                    .padding(start = 40.dp, top = 4.dp)
                    .height(40.dp)
            ) {
                Text(
                    text = "Tippe auf eine Person um weitere Funktionen zu sehen",
                    fontSize = 16.sp
                )
            }

            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .padding(start = 0.dp, top = 16.dp)
                    .horizontalScroll(scrollState)

            ) {
                Column(modifier = Modifier.width(40.dp)) {}
                persons.forEach { person ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = { toContact(person) })

                    ) {
                        LoadPersonCardContent(person.name, person.avatarUrl)
                    }
                }
            }

            // Back and Forward Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 40.dp)
                    .fillMaxHeight()
                    .padding(16.dp)
                    .height(80.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val scope = rememberCoroutineScope()

                // Backwards
                // onClick adds or subtracts 500 to/from current scrollState
                ScrollNavigationButton(
                    onClick = {
                        scope.launch {
                            scrollState.scrollTo(
                                scrollState.value -
                                    UIConstants.ScrollButton.SCROLL_DISTANCE
                            )
                        }
                    },
                    type = ScrollButtonType.PREVIOUS,
                    text = "Vorherige Seite",
                    scrollState = scrollState,
                )

                // Forwards
                ScrollNavigationButton(
                    onClick = {
                        scope.launch {
                            scrollState.scrollTo(
                                scrollState.value +
                                    UIConstants.ScrollButton.SCROLL_DISTANCE
                            )
                        }
                    },
                    type = ScrollButtonType.NEXT,
                    text = "Nächste Seite",
                    scrollState = scrollState
                )
            }
        }
    }

    private fun backToHome() {
        navigator.toHome()
    }

    private fun toContact(person: Person) {
        navigator.toPersonDetail(person)
    }

}