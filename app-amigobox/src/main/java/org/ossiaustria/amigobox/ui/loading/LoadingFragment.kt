package org.ossiaustria.amigobox.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.R

@AndroidEntryPoint
class LoadingFragment : Fragment() {

//    lateinit var loginButton: Button

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { LoadingFragmentComposable() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        root = view
        // init stuff
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

            }
        }
    }

    // reuse a composeable - there are no styles
    @Composable
    fun MaterialButton(
        onClick: () -> Unit,
        text: String
    ) {
        TextButton(
            onClick = onClick,
            Modifier.fillMaxWidth()
        ) {
            Text(text = text)
        }
    }

    // private composable/view methods
    private fun startTimeline() {
        root.findNavController()
            .navigate(LoadingFragmentDirections.actionLoadingFragmentToTimelineFragment())
    }

    private fun startJitsi() {
        root.findNavController()
            .navigate(LoadingFragmentDirections.actionLoadingFragmentToJitsiFragment())
    }

    private fun startLogin() {
        root.findNavController()
            .navigate(LoadingFragmentDirections.actionLoadingFragmentToLoginFragment())
    }

}
