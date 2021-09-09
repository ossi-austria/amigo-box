package org.ossiaustria.amigobox.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.contacts.GlobalStateViewModel
import org.ossiaustria.amigobox.ui.commons.MaterialButton
import org.ossiaustria.lib.domain.modules.UserContext
import javax.inject.Inject

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    private val globalState: GlobalStateViewModel by activityViewModels()

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var userContext: UserContext

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { LoadingFragmentComposable() }
    }

    @Composable
    fun LoadingFragmentComposable() {
        MaterialTheme {
            Column {
                Text("")
                MaterialButton(
                    onClick = { startLogin() },
                    text = stringResource(R.string.onboarding_login_label)
                )

                MaterialButton(onClick = { startJitsi() }, text = "Start jitsi")
                MaterialButton(onClick = { startTimeline() }, text = "Show Timeline")
                MaterialButton(onClick = { startContacts() }, text = "Show Contacts")
                MaterialButton(onClick = { startAlbums() }, text = "Show Albums")
                MaterialButton(onClick = { startGallery() }, text = "Show Gallery")
                MaterialButton(
                    onClick = { startPersonDetail() },
                    text = "Person detail (currentUser)"
                )
                MaterialButton(onClick = { startHome2() }, text = "Show HomeFragment")

            }
        }
    }

    private fun startPersonDetail() {
        val person = userContext.person()
        if (person != null) {
            globalState.setCurrentPerson(person)
            navigator.toPersonDetail()
        } else {
            Toast.makeText(requireContext(), "Nicht eingeloggt!", Toast.LENGTH_LONG).show()
        }
    }

    // private composable/view methods
    private fun startTimeline() {
        navigator.toTimeline()
    }

    private fun startJitsi() {
        navigator.toJitsiCall()
    }

    private fun startLogin() {
        navigator.toLogin()
    }

    private fun startContacts() {
        navigator.toContacts()
    }

    private fun startAlbums() {
        navigator.toAlbums()
    }

    private fun startGallery() {
        navigator.toImageGallery()
    }

    private fun startHome() {
        navigator.toHome()
    }

    private fun startHome2() {
        navigator.toHome2()
    }
}
