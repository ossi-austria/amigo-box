package org.ossiaustria.amigobox.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.R

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.loading_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton = view.findViewById(R.id.loginButton)

        val jitsi_button = view.findViewById<Button>(R.id.jitsi_button)


        loginButton.setOnClickListener {
            view.findNavController()
                .navigate(LoadingFragmentDirections.actionLoadingFragmentToLoginFragment())
        }

        // init logic
        jitsi_button.setOnClickListener{
            view.findNavController()
                .navigate(LoadingFragmentDirections.actionLoadingFragmentToJitsiFragment())
        }

    }
}