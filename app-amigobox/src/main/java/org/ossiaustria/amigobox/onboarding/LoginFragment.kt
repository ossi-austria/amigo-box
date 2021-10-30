package org.ossiaustria.amigobox.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.R

class LoginFragment : Fragment() {

    // Retrieve OnboardingViewModel via injection
    private val viewModel by viewModel<OnboardingViewModel>()

    // use "lateinit var" for not-null GUI fields
    lateinit var loginButton: Button
    lateinit var emailEdit: EditText
    lateinit var passwordEdit: EditText
    lateinit var statusText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // initialise views ONCE in beginning
        loginButton = view.findViewById(R.id.loginButton)
        emailEdit = view.findViewById(R.id.emailEdit)
        passwordEdit = view.findViewById(R.id.passwordEdit)
        statusText = view.findViewById(R.id.statusText)

        loginButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            viewModel.login(email, password)
        }
    }

    /**
     * onResume is called when Fragment gets activated (also screen turn on)
     */
    override fun onResume() {
        super.onResume()

        // activate observers
        viewModel.state.observe(viewLifecycleOwner) { state: OnboardingState ->
            if (state is OnboardingState.LoginSuccess) {
                Toast.makeText(context, "Erfolgreich eingeloggt", Toast.LENGTH_LONG).show()
                statusText.text = state.account.toString()
            } else if (state is OnboardingState.LoginFailed) {
                statusText.text = state.exception.toString()
            } else {
                statusText.text = state.toString()
            }
        }
    }

    /**
     * onPause is called when Fragment gets activated, paused, stopped and is about to vanish.
     * Use ONLY onPause, not overcomplicate with onDestroy etc
     */
    override fun onPause() {
        super.onPause()

        /**
         * As the fragment is not active, we have to deactive observers, as we cannot react to changes right now and would lose them
         */
        viewModel.state.removeObservers(viewLifecycleOwner)
    }

}