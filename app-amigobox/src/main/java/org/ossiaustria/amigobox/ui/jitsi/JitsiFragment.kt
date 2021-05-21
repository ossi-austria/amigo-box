package org.ossiaustria.amigobox.ui.jitsi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.facebook.react.modules.core.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetView
import org.ossiaustria.amigobox.R
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class JitsiFragment : Fragment(), JitsiMeetActivityInterface {

    private lateinit var view: JitsiMeetView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.jitsi_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jitsiView = JitsiMeetView(requireActivity())
        val root = view.findViewById<ConstraintLayout>(R.id.root)
        root.addView(jitsiView)
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom("https://meet.jit.si/test123")
            .build()
        jitsiView.join(options)
    }

    override fun onDestroy() {
        super.onDestroy()
        view.dispose()
        //view = null
        JitsiMeetActivityDelegate.onHostDestroy(this.activity)
    }

    @SuppressLint("MissingSuperCall")
    fun onNewIntent(intent: Intent) {
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        //  request INTERNET
        if (p0 != null) {
            ActivityCompat.requestPermissions(this.requireActivity(), p0, p1)
        }
    }

    override fun checkPermission(permission: String?, requestCode: Int, p2: Int): Int {

        // check CAMERA
        val cameraPermission = ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.CAMERA
        )
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            Timber.d("checkSelfPermission -> has CAMERA")
        } else {
            Timber.tag(TAG).v("has not CAMERA")
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(permission),
                requestCode
            )
        }
        // check RECORD_AUDIO
        val audioPermission = ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.RECORD_AUDIO
        )
        if (audioPermission == PackageManager.PERMISSION_GRANTED) {
            Timber.tag(TAG).v("has RECORD_AUDIO")
        } else {
            Timber.tag(TAG).v("has not RECORD_AUDIO")
            // request RECORD_AUDIO
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(permission),
                requestCode
            )
        }

        if (Objects.equals(
                ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.MODIFY_AUDIO_SETTINGS
                ), PackageManager.PERMISSION_GRANTED
            )
        ) {
            Timber.tag(TAG).v("has MODIFY_AUDIO_SETTINGS ")
        } else {
            Timber.tag(TAG).v("has not MODIFY_AUDIO_SETTINGS")

            // request MODIFY_AUDIO_SETTINGS
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(permission),
                requestCode
            )
        }

        // 0 means ok ?
        return 0
    }

    override fun checkSelfPermission(permission: String?): Int {
        Timber.d("checkSelfPermission called, but not used")
        return ActivityCompat.checkSelfPermission(requireContext(), permission ?: "")
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this.activity)
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this.activity)
    }

    companion object {
        const val TAG = "checkSelfPermission ->"
    }

}