package org.ossiaustria.amigobox

import android.Manifest.permission.CAMERA
import android.Manifest.permission.CAPTURE_AUDIO_OUTPUT
import android.Manifest.permission.INTERNET
import android.Manifest.permission.MODIFY_AUDIO_SETTINGS
import android.Manifest.permission.NFC
import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.SYSTEM_ALERT_WINDOW
import android.Manifest.permission.USE_FULL_SCREEN_INTENT
import android.Manifest.permission.VIBRATE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.CallPerson
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.Error
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.Failure
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.OpenAlbum
import org.ossiaustria.amigobox.nfc.NfcViewModelEvent.TagLost
import org.ossiaustria.lib.domain.services.events.IncomingEventCallbackService
import org.ossiaustria.lib.nfc.InternalNfcWrapper
import org.ossiaustria.lib.nfc.NfcEvent
import org.ossiaustria.lib.nfc.NfcIntentHandler
import timber.log.Timber

open class MainBoxActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    val navigator: Navigator by inject()
    private val cloudPushHandlerService: CloudPushHandlerService by inject()
    private val incomingEventCallbackService: IncomingEventCallbackService by inject()
    protected val nfcViewModel: NfcViewModel by viewModel()
    private val ioDispatcher: CoroutineDispatcher by inject()
    private val scope = CoroutineScope(ioDispatcher)
    private var job: Job? = null

    // NFC adapter for checking NFC state in the device
    protected val internalNfcWrapper: InternalNfcWrapper = InternalNfcWrapper()
    private val nfcIntentHandler: NfcIntentHandler = NfcIntentHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
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

        checkPermissionsVideoCall {}
        checkPermissionsBasics {
            onCreateSetupNfcIntentHandling()
            handleNfcIntent(intent)
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

        job = scope.launch {
            incomingEventCallbackService.callEventFlow
                .onEach { navigator.toCallFragment(it) }
                .onCompletion { cause -> if (cause == null) Timber.i("Flow is completed successfully") }
                .catch { cause -> Timber.e("Exception $cause happened") }
        }
        nfcViewModel.nfcEvent.observe(this) { resource ->
            when (resource) {
                is OpenAlbum -> nfcViewModel.openAlbum(resource.album, navigator)
                is CallPerson -> nfcViewModel.callPerson(resource.person, navigator)
                is Error -> {
                    Toast.makeText(
                        this,
                        "NFC-Tag Error: ${resource.exception.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Failure -> {
                    Toast.makeText(this, "NFC-Tag Failure ${resource.message}", Toast.LENGTH_LONG)
                        .show()
                }
                is TagLost -> {
                    Toast.makeText(this, "TagLost", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "resource $resource", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        internalNfcWrapper.startDetecting(this)
    }

    private fun handleCallIntents(intent: Intent) {
        intent.extras?.let { bundle ->
            Navigator.getCall(bundle)?.let { call ->
                Navigator.setCall(bundle, null)
                checkPermissionsVideoCall { navigator.toCallFragment(call) }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        internalNfcWrapper.stopDetecting(this)
        nfcViewModel.nfcEvent.removeObservers(this)

        job?.cancel()
    }

    open fun onCreateSetupNfcIntentHandling() {
        // implement nfcAdapter Object
        internalNfcWrapper.setupDeviceOnce(this)
    }

    private fun handleNfcIntent(checkIntent: Intent?) {
        val nfcTagData = nfcIntentHandler.processNfcIntent(checkIntent)
        if (nfcTagData != null) {
            nfcViewModel.processNfcTagData(NfcEvent.TagFound(nfcTagData))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
        handleCallIntents(intent)
    }

    override fun onDestroy() {
        cloudPushHandlerService.unbind()
        super.onDestroy()
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
            block.invoke()
        }
    }

    @AfterPermissionGranted(VIDEO_CALL_REQUEST_CODE)
    fun checkPermissionsVideoCall(block: () -> Unit) {
        checkPermissions(
            VIDEO_CALL_REQUEST_CODE,
            listOf(CAMERA, RECORD_AUDIO, MODIFY_AUDIO_SETTINGS, CAPTURE_AUDIO_OUTPUT),
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
            Timber.w("onPermissionsDenied: $requestCode -> $it")
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

