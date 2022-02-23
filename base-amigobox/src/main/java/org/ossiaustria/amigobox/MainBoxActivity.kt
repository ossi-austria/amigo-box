package org.ossiaustria.amigobox

import android.Manifest.permission.CAMERA
import android.Manifest.permission.INTERNET
import android.Manifest.permission.MODIFY_AUDIO_SETTINGS
import android.Manifest.permission.NFC
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.SYSTEM_ALERT_WINDOW
import android.Manifest.permission.USE_FULL_SCREEN_INTENT
import android.Manifest.permission.VIBRATE
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.ossiaustria.amigobox.cloudmessaging.CloudPushHandlerService
import org.ossiaustria.amigobox.nfc.NfcViewModel
import org.ossiaustria.amigobox.nfc.NfcViewModelState
import org.ossiaustria.lib.domain.services.events.IncomingEventCallbackService
import org.ossiaustria.lib.nfc.NfcHandler
import timber.log.Timber

class MainBoxActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    val navigator: Navigator by inject()
    private val cloudPushHandlerService: CloudPushHandlerService by inject()
    private val incomingEventCallbackService: IncomingEventCallbackService by inject()
    private val nfcViewModel: NfcViewModel by viewModel()
    private val ioDispatcher: CoroutineDispatcher by inject()
    private val scope = CoroutineScope(ioDispatcher)
    private var job: Job? = null

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
            navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        )

        ActivityHelper.prepareActivityForDeviceLock(this)

        // NOTE: must be called in every Activity
        if (savedInstanceState == null) {
            navigator.toLoading()
        }

        cloudPushHandlerService.bindToActivity(this)

        adaptWindow()

        checkPermissionsBasics {
            onCreateSetupNfcIntentHandling()
            checkPermissionsIncomingCall {}
        }
        handleCallIntents(intent)
    }

    private fun adaptWindow() {
        supportActionBar?.hide()
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
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

        job = scope.launch {
            incomingEventCallbackService.callEventFlow
                .onEach { navigator.toCallFragment(it) }
                .onCompletion { cause -> if (cause == null) println("Flow is completed successfully") }
                .catch { cause -> println("Exception $cause happened") }
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
                checkPermissionsVideoCall {
                    navigator.toCallFragment(call)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        nfcAdapter?.disableForegroundDispatch(this)
        nfcViewModel.state.removeObservers(this)

        job?.cancel()
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

    private fun checkPermissions(
        requestCode: Int,
        permissions: List<String>,
        explanation: String,
        block: () -> Unit
    ) {
        if (EasyPermissions.hasPermissions(this, *permissions.toTypedArray())) {
            Timber.i("Permissions are given: $permissions")
            block.invoke()
        } else {
            Timber.w("Permissions are missing: $permissions")

            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                explanation,
                requestCode,
                *permissions.toTypedArray()
            )
        }
    }

    @AfterPermissionGranted(VIDEO_CALL_REQUEST_CODE)
    fun checkPermissionsVideoCall(block: () -> Unit) {
        checkPermissions(
            VIDEO_CALL_REQUEST_CODE,
            listOf(CAMERA, RECORD_AUDIO, MODIFY_AUDIO_SETTINGS),
            "Need for Video",
            block
        )
    }

    @AfterPermissionGranted(BASIC_REQUEST_CODE)
    fun checkPermissionsBasics(block: () -> Unit) {
        checkPermissions(
            BASIC_REQUEST_CODE,
            listOf(
                INTERNET,
                NFC,
            ), "Need for Basics", block
        )
    }

    @AfterPermissionGranted(INCOMING_CALL_REQUEST_CODE)
    fun checkPermissionsIncomingCall(block: () -> Unit) {

        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            listOf(VIBRATE, USE_FULL_SCREEN_INTENT)
        } else {
            listOf(VIBRATE, SYSTEM_ALERT_WINDOW)
        }

        checkPermissions(
            INCOMING_CALL_REQUEST_CODE,
            permissions, "Need for Incoming Videos", block
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Timber.i("onPermissionsGranted: $requestCode :${perms.size}")
        perms.forEach {
            Timber.i("onPermissionsGranted: $requestCode -> $it")
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        Timber.d("onPermissionsDenied: $requestCode :${perms.size}")
        perms.forEach {
            Timber.i("onPermissionsDenied: $requestCode -> $it")
        }
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            SettingsDialog.Builder(this).build().show()
        }
    }

    companion object {
        const val BASIC_REQUEST_CODE = 1010
        const val VIDEO_CALL_REQUEST_CODE = 1020
        const val INCOMING_CALL_REQUEST_CODE = 1030
    }
}

