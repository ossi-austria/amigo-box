package org.ossiaustria.amigobox.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.databinding.TimelineFragmentBinding
import timber.log.Timber

class TimelineFragment : Fragment() {

    // Retrieve OnboardingViewModel via injection
    private val viewModel by viewModel<TimelineViewModel>()

    // Use "ViewBinding" to load the view nicely
    private lateinit var binding: TimelineFragmentBinding

    private val adapter = TimelineAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //use ViewBinding to inflate
        binding = TimelineFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // access view directly via ViewBinding
        binding.sendablesView.adapter = adapter
        binding.sendablesView.layoutManager = LinearLayoutManager(view.context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAllSendables()
        // activate observers
        viewModel.sendables.observe(viewLifecycleOwner) { list ->
            adapter.setSendables(list)
            Timber.i("Loaded list: ${list.size}")
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.sendables.removeObservers(viewLifecycleOwner)
    }
}