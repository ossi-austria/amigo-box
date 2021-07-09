package org.ossiaustria.amigobox

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.WindowManager.LayoutParams.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.facebook.react.modules.core.PermissionListener
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import dagger.hilt.android.AndroidEntryPoint
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.ossiaustria.lib.nfc.NfcConstants
import org.ossiaustria.lib.nfc.NfcHandler
import javax.inject.Inject

interface NavControllerProvider {

}


@AndroidEntryPoint
class MainBoxActivity : AppCompatActivity(), JitsiMeetActivityInterface {

    @Inject
    lateinit var navigator: Navigator

    // NFC adapter for checking NFC state in the device
    private var nfcAdapter: NfcAdapter? = null

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null
    private val nfcHandler: NfcHandler = NfcHandler()

    private var nfcInfo: NfcHandler.NfcInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        navigator.bind((supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController)

        ActivityHelper.prepareActivityForDeviceLock(this)

        // NOTE: must be called in every Activity

        if (savedInstanceState == null) {
            navigator.toLoading()
        }

        initNFC()
    }

    override fun onResume() {
        super.onResume()

        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        nfcInfo = nfcHandler.processNfcIntent(intent)

        if (nfcInfo?.type == NfcConstants.PREFIX) {
            // NFC Message is stored in nfcHandler.inNfcMessage
            Toast.makeText(this, nfcInfo?.message, Toast.LENGTH_SHORT).show()
            // UID of NFC-Tag ist stored in nfcHandler.nfcTagId
            Toast.makeText(this, nfcInfo?.tagId, Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(this, "NFC-Tag ungültig", Toast.LENGTH_SHORT).show()

    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        val permissions =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
            )
        Permissions.check(
            this /*context*/,
            permissions,
            null /*rationale*/,
            null /*options*/,
            object : PermissionHandler() {
                override fun onGranted() {
                    Toast.makeText(this@MainBoxActivity, "Premission granted", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun initNFC() {
        // implement nfcAdapter Object
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Read all tags when app is running and in the foreground
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

        nfcInfo = nfcHandler.processNfcIntent(intent)
        if (nfcInfo?.type == NfcConstants.PREFIX) {
            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, nfcInfo?.message, Toast.LENGTH_SHORT).show()
        }
    }
}
