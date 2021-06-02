package org.ossiaustria.lib.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import timber.log.Timber

class NfcHandler {

    data class NfcInfo(
        val message: String,
        val tagId: String,
        val type: String
    )

    fun processNfcIntent(checkIntent: Intent?): NfcInfo? {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent?.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val nfcUidByteArray = checkIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID)

            if (rawMessages != null && rawMessages.isNotEmpty() && nfcUidByteArray != null) {
                val ndefMsg = rawMessages[0] as NdefMessage
                if (ndefMsg.records != null && ndefMsg.records.isNotEmpty()) {
                    val ndefRecord = ndefMsg.records[0]

                    val type = String(ndefRecord.type)
                    val inNfcMessage = String(ndefRecord.payload)

                    var nfcTagId = ""
                    for (byte in nfcUidByteArray) {
                        val hexValue = String.format("%02X", byte)

                        nfcTagId = nfcTagId.plus(hexValue)
                    }

                    return NfcInfo(inNfcMessage, nfcTagId, type)
                }
                // not sure why this is unreachable

                else {
                    Timber.w("NDEF message is empty, NFC-Tag can not be used")
                    return null
                }
            } else {
                Timber.w("Some part of NFC data is null, Tag can not be used")
                return null
            }
        } else {
            Timber.w("Intent has no NFC data attached")
            return null
        }
    }
}
