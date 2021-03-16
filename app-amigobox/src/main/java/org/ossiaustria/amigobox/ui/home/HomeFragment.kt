package org.ossiaustria.amigobox.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.amigobox.R

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val message = view.findViewById<TextView>(R.id.message)

        viewModel.liveAlbums.observe(viewLifecycleOwner) { albums ->
            message.text = if (albums.isNotEmpty()) {
                albums.map { it.name }.reduce { all, next -> "$all $next" }
            } else {
                "empty"
            }
        }
    }

}