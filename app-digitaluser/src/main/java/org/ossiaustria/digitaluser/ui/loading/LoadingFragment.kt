package org.ossiaustria.digitaluser.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.digitaluser.R

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    lateinit var registerButton: Button
    lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.loading_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerButton = view.findViewById(R.id.registerButton)
        loginButton = view.findViewById(R.id.loginButton)
        val jitsiButton = view.findViewById<Button>(R.id.jitsiButton)

        registerButton.setOnClickListener {
            view.findNavController()
                .navigate(LoadingFragmentDirections.actionLoadingFragmentToRegisterFragment())
        }

        loginButton.setOnClickListener {
            view.findNavController()
                .navigate(LoadingFragmentDirections.actionLoadingFragmentToLoginFragment())
        }

        // init logic
        jitsiButton.setOnClickListener {
            view.findNavController()
                .navigate(LoadingFragmentDirections.actionLoadingFragmentToJitsiFragment())
        }

    }
}