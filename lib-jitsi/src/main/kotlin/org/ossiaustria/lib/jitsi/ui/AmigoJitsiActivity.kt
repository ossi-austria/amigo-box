package org.ossiaustria.lib.jitsi.ui

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.react.modules.core.PermissionListener
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.ossiaustria.lib.jitsi.R

class AmigoJitsiActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    private var jitsiFragment: JitsiFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jitsi_activity)
        supportFragmentManager.beginTransaction().apply {
            jitsiFragment = JitsiFragment()
            add(jitsiFragment!!, null)
            commit()
        }
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        val permissions =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
            )
        Permissions.check(this, permissions, null, null,
            object : PermissionHandler() {
                override fun onGranted() {
                    Toast.makeText(
                        this@AmigoJitsiActivity,
                        "Permission granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        JitsiMeetActivityDelegate.onNewIntent(intent)
        super.onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
        jitsiFragment?.startCall()
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this)
    }
}