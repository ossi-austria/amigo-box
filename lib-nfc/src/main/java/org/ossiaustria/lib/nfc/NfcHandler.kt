package org.ossiaustria.lib.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import timber.log.Timber

class NfcHandler {

    data class NfcTagData(
        val tagId: String,
        val message: String? = null,
        val type: String? = null
    )

    fun processNfcIntent(checkIntent: Intent?): NfcTagData? {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val rawByteArray = checkIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
            return if (rawByteArray != null) {
                val nfcTagId = extractNfcId(rawByteArray)
                NfcTagData(nfcTagId)
            } else {
                null
            }
        } else if (checkIntent?.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val nfcUidByteArray = checkIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID)

            if (rawMessages != null && rawMessages.isNotEmpty() && nfcUidByteArray != null) {
                val ndefMsg = rawMessages[0] as NdefMessage
                if (ndefMsg.records != null && ndefMsg.records.isNotEmpty()) {
                    val ndefRecord = ndefMsg.records[0]

                    val type = String(ndefRecord.type)
                    val inNfcMessage = String(ndefRecord.payload)

                    val nfcTagId = extractNfcId(nfcUidByteArray)

                    return NfcTagData(nfcTagId, inNfcMessage, type)
                } else {
                    Timber.w("NDEF message is empty, NFC-Tag can not be used")
                    return null
                }
            } else {
                Timber.w("Some part of NFC data is null, Tag can not be used")
                return null
            }
        } else {
            return null
        }
    }

    private fun extractNfcId(nfcUidByteArray: ByteArray): String {
        var nfcTagId = ""
        for (byte in nfcUidByteArray) {
            val hexValue = String.format("%02X", byte)

            nfcTagId = nfcTagId.plus(hexValue)
        }
        return nfcTagId
    }
}
