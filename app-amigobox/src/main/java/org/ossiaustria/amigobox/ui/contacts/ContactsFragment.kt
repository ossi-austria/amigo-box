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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.contacts.GlobalStateViewModel
import org.ossiaustria.amigobox.ui.UIConstantsAmigoBox
import org.ossiaustria.amigobox.ui.commons.NavigationButton
import org.ossiaustria.amigobox.ui.commons.ScrollButtonType
import org.ossiaustria.amigobox.ui.commons.ScrollNavigationButton
import org.ossiaustria.lib.domain.models.Person
import javax.inject.Inject

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private val globalState: GlobalStateViewModel by activityViewModels()

    @Inject
    lateinit var navigator: Navigator

    private val viewModel by viewModels<ContactsViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { ContactsScreen() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        globalState.selectedPerson.observe(viewLifecycleOwner) {

        }
    }

    @Composable
    fun ContactsScreen() {
        MaterialTheme {
            ContactsFragmentComposable()
        }
    }

    @Composable
    fun ContactsFragmentComposable() {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        )
        {

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


            // Scrollable List of Contacts and Name of each Contact

            val scrollState = rememberScrollState()
            // Timber.w("Scrollstate: %s", scrollState.value.toString())
            val scope = rememberCoroutineScope()

            Row(

                modifier = Modifier
                    .padding(start = 0.dp, top = 16.dp)
                    .horizontalScroll(scrollState)


            ) {

                listOfPeopleWithImages().forEach { person ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(
                            onClick = { toContact(person.person) }
                        )

                    )
                    {
                        Column(
                            //modifier = Modifier.verticalScroll(rememberScrollState())
                        ) {
                            LoadPersonCardContent(person)
                        }


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

                // Backwards
                // onClick adds or subtracts 500 to/from current scrollState
                ScrollNavigationButton(
                    onClick = {
                        scope.launch { scrollState.scrollTo(scrollState.value -
                            UIConstantsAmigoBox.ScrollButton.SCROLL_DISTANCE) }
                    },
                    type = ScrollButtonType.PREVIOUS,
                    text = "Vorherige Seite",
                    scrollState = scrollState,
                )


                // Forwards
                ScrollNavigationButton(
                    onClick = {
                        scope.launch { scrollState.scrollTo(scrollState.value +
                            UIConstantsAmigoBox.ScrollButton.SCROLL_DISTANCE) }
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


    //TODO: input is a Person object
    // not sure, is that correct... person as input?
// using globalState.setCurrentPerson method, to set Person
    private fun toContact(person: Person) {
        globalState.setCurrentPerson(person)
        navigator.toPersonDetail()
    }


}