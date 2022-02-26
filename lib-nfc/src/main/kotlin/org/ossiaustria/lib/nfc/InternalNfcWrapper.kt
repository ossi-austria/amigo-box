package org.ossiaustria.lib.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import timber.log.Timber

interface NfcWrapper {
}

class InternalNfcWrapper : NfcWrapper {

    // NFC adapter for checking NFC state in the device
    private var nfcAdapter: NfcAdapter? = null

    // Pending intent for NFC intent foreground dispatch.
    // Used to read all NDEF tags while the app is running in the foreground.
    private var nfcPendingIntent: PendingIntent? = null

    private var isSetup = false

    fun setupDeviceOnce(activity: Activity) {

        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)

        // Read all tags when app is running and in the foreground
        // Create a generic PendingIntent that will be deliver to this activity. The NFC stack
        // will fill in the intent with the details of the discovered tag before delivering to
        // this activity.
        nfcPendingIntent = PendingIntent.getActivity(
            activity, 0,
            Intent(activity, activity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_UPDATE_CURRENT
        )
        isSetup = true
    }

    fun startDetecting(activity: Activity) {
        if (!isSetup) {
            setupDeviceOnce(activity)
        }
        // Get all NDEF discovered intents
        // Makes sure the app gets all discovered NDEF messages as long as it's in the foreground.
        nfcAdapter!!.enableForegroundDispatch(activity, nfcPendingIntent, null, null)
    }

    fun stopDetecting(activity: Activity) {
        Timber.i("stopDetecting")
        nfcAdapter?.disableForegroundDispatch(activity)
    }
}