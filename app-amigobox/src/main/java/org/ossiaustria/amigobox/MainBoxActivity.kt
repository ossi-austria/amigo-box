package org.ossiaustria.amigobox

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.calls.IncomingEventsViewModel
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.nfc.NfcViewModel
import org.ossiaustria.amigobox.nfc.NfcViewModelState
import org.ossiaustria.lib.nfc.NfcHandler

class MainBoxActivity : AppCompatActivity() {

    val navigator: Navigator by inject()
    private val cloudPushHandlerService: CloudPushHandlerService by inject()
    private val nfcViewModel: NfcViewModel by viewModel()
    private val incomingEventsViewModel: IncomingEventsViewModel by viewModel()

    // NFC adapter for checking NFC state in the device
    private var nfcAdapter: NfcAdapter? = null

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null
    private val nfcHandler: NfcHandler = NfcHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        navigator.bind(
            activity = this,
            navController = (
                supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                ).navController
        )

        ActivityHelper.prepareActivityForDeviceLock(this)

        // NOTE: must be called in every Activity
        if (savedInstanceState == null) {
            navigator.toLoading()
        }

        onCreateSetupNfcIntentHandling()

        handleCallIntents(intent)

        cloudPushHandlerService.bindToActivity(this)
        // FIXME: Add Permission util again and ask for ACTION_MANAGE_OVERLAY_PERMISSION
    }

    override fun onResume() {
        super.onResume()

        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)

        nfcViewModel.nfcInfo.observe(this) { resource ->
            if (resource.isSuccess) {
                val nfcInfo = resource.valueOrNull()
                if (nfcInfo != null) {
                    // NFC Message is stored in nfcHandler.inNfcMessage
                    val message = "NFC detected: ${nfcInfo.name} ${nfcInfo.nfcRef} ${nfcInfo.type}"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    nfcViewModel.handleNfcInfo(nfcInfo)
                } else {
                    Toast.makeText(this, "NFC-Tag ungültig", Toast.LENGTH_SHORT).show()
                }
            }
        }

        incomingEventsViewModel.notifiedCall.observe(this) {
            it?.let {
                navigator.toCallFragment(it)
                incomingEventsViewModel.clearCall()
            }
        }

        nfcViewModel.state.observe(this) { resource ->
            if (resource.isSuccess) {
                when (val currentState = resource.valueOrNull()) {
                    is NfcViewModelState.OpenAlbum -> nfcViewModel.openAlbum(
                        currentState.album,
                        navigator
                    )
                    is NfcViewModelState.CallPerson -> nfcViewModel.callPerson(
                        currentState.person,
                        navigator
                    )
                    is NfcViewModelState.Error -> {
                        Toast.makeText(this, "NFC-Tag ungültig", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (resource.isFailure) {
                Toast.makeText(this, resource.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleCallIntents(intent: Intent) {
        intent.extras?.let { bundle ->
            Navigator.getCall(bundle)?.let { call ->
                Navigator.setCall(bundle, null)
                navigator.toCallFragment(call)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(this)
        nfcViewModel.state.removeObservers(this)
    }

    private fun onCreateSetupNfcIntentHandling() {
        // implement nfcAdapter Object
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // Read all tags when app is running and in the foreground
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT
        )

        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(checkIntent: Intent?) {
        val nfcInfo = nfcHandler.processNfcIntent(checkIntent)
        if (nfcInfo != null) {
            nfcViewModel.processNfcTagData(nfcInfo)
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
        handleCallIntents(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        cloudPushHandlerService.unbind()
    }

    companion object {
        const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 3456
    }
}

