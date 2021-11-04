package org.ossiaustria.amigobox.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.amigobox.onboarding.OnboardingState
import org.ossiaustria.amigobox.onboarding.OnboardingViewModel
import org.ossiaustria.amigobox.ui.UIConstants
import org.ossiaustria.amigobox.ui.commons.AmigoThemeLight
import org.ossiaustria.amigobox.ui.commons.MaterialButton
import org.ossiaustria.amigobox.ui.commons.TextAndIconButton
import org.ossiaustria.amigobox.ui.commons.textResString
import timber.log.Timber

class LoadingFragment : Fragment() {

    val navigator: Navigator by inject()

    private val viewModel by viewModel<OnboardingViewModel>()

    // needed in near future
    private val loginTokenAuthMethodEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { LoadingFragmentScreen(viewModel) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadAccount()
    }

    override fun onResume() {
        super.onResume()
        viewModel.bind(navigator)

        viewModel.state.observe(viewLifecycleOwner) {
            Timber.i("$it")
            if (it is OnboardingState.IsLoggedIn) {
                navigator.toHome()
            } else if (it is OnboardingState.LoginSuccess) {
                navigator.toHome()
            } else {
                Timber.i("$it")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.state.removeObservers(viewLifecycleOwner)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }

    @Composable
    fun LoadingFragmentScreen(viewModel: OnboardingViewModel) {
        AmigoThemeLight {
            val state: OnboardingState by viewModel.state.observeAsState(OnboardingState.NotLoggedIn)
            Surface(color = MaterialTheme.colors.background) {
                LoadingFragmentContent(
                    state = state,
                    login = viewModel::login,
                    loginPerToken = viewModel::loginPerToken,
                    loginTokenAuthMethodEnabled = loginTokenAuthMethodEnabled
                )
            }
        }
    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun LoadingFragmentContent(
    state: OnboardingState,
    login: (String, String) -> Unit,
    loginPerToken: (String) -> Unit,
    loginTokenAuthMethodEnabled: Boolean = false
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier
                .width(UIConstants.Defaults.BOX_WIDTH)
                .height(IntrinsicSize.Min)
        ) {
            if (state is OnboardingState.IsLoggedIn) {
                Loading()
            } else if (state is OnboardingState.LoginSuccess) {
                Loading()
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(UIConstants.Defaults.INNER_PADDING)
                ) {
                    var email by rememberSaveable { mutableStateOf("analogue@example.org") }
                    var password by rememberSaveable { mutableStateOf("password") }
                    var loginToken by rememberSaveable { mutableStateOf("") }

                    val keyboardController = LocalSoftwareKeyboardController.current

                    val isError = state is OnboardingState.LoginFailed
                    val loginEnabled = email.length > 5 && password.length > 5
                    val loginTokenEnabled = loginToken.length > 3

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(UIConstants.Defaults.INNER_PADDING),
                        text = stringResource(R.string.onboarding_login_label),
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.secondary
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(UIConstants.Defaults.INNER_PADDING),
                        value = email,
                        isError = isError,
                        label = { stringResource(R.string.onboarding_email_label) },
                        onValueChange = { email = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(UIConstants.Defaults.INNER_PADDING),
                        value = password,
                        isError = isError,
                        label = { stringResource(R.string.onboarding_password_label) },
                        onValueChange = { password = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }),
                    )
                    TextAndIconButton(
                        R.drawable.ic_login,
                        stringResource(R.string.onboarding_login_button),
                        contentColor = MaterialTheme.colors.onSecondary,
                        backgroundColor = MaterialTheme.colors.secondary,
                        onClick = { login(email, password) }
                    )
                    if (loginTokenAuthMethodEnabled) {
                        Text(".. oder per Token")
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = loginToken,
                            label = { textResString(R.string.onboarding_login_token_label) },
                            onValueChange = { loginToken = it },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }),
                        )
                        MaterialButton(
                            onClick = { loginPerToken(loginToken) },
                            enabled = loginTokenEnabled,
                            text = textResString(R.string.onboarding_login_button),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LoadingFragmentContentPreview_NotLoggedIn() {
    AmigoThemeLight {
        LoadingFragmentContent(
            OnboardingState.NotLoggedIn,
            { _, _ -> },
            { },
        )
    }
}
