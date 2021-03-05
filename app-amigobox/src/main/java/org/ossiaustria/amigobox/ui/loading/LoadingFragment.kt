package org.ossiaustria.amigobox.ui.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.R

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    private val viewModel by viewModels<LoadingViewModel>()

    lateinit var message: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.loading_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.button)
        message = view.findViewById(R.id.message)

        button.setOnClickListener {
            viewModel.doFancyHeavyStuffOnBackground()
//            view.findNavController()
//                .navigate(LoadingFragmentDirections.actionLoadingFragmentToLoginFragment())
        }

        // observer
        viewModel.liveUserLogin.observe(viewLifecycleOwner) { user: String ->
            message.text = user
        }

        // init logic

    }

}