package org.ossiaustria.lib.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import timber.log.Timber

class NfcHandler {

    data class NfcInfo(
        val message: String,
        val tagId: String
    )

    fun processNfcIntent(checkIntent: Intent?): NfcInfo? {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent?.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            var inNfcMessage = ""
            var nfcTagId = ""
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)

            if (rawMessages != null && rawMessages.isNotEmpty()) {
                val ndefMsg = rawMessages[0] as NdefMessage
                if (ndefMsg.records != null && ndefMsg.records.isNotEmpty()) {
                    val ndefRecord = ndefMsg.records[0]

                    inNfcMessage = String(ndefRecord.payload)
                }
            }

            val nfcUidByteArray = checkIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID)

            if (nfcUidByteArray != null) {
                for (byte in nfcUidByteArray) {
                    val hexValue = String.format("%02X", byte)
                    nfcTagId = nfcTagId.plus(hexValue)
                }
            }
            return NfcInfo(inNfcMessage, nfcTagId)
        } else {
            Timber.w("Intent has no NFC data attached")
            return null
        }
    }
}
