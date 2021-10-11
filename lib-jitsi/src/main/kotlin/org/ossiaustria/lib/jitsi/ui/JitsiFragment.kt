package org.ossiaustria.lib.jitsi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetView
import org.ossiaustria.lib.jitsi.R

//@AndroidEntryPoint
class JitsiFragment : Fragment() {

    private var jitsiView: JitsiMeetView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.jitsi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jitsiView = JitsiMeetView(requireActivity())
        val root = view.findViewById<FrameLayout>(R.id.root)
        root.addView(jitsiView)
    }

    fun startCall() {
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom("https://meet.jit.si/test123")
            .build()
        jitsiView?.join(options)
    }

    override fun onDestroy() {
        super.onDestroy()
        jitsiView?.dispose()
    }

}