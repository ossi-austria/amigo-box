package org.ossiaustria.lib.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import timber.log.Timber

class NfcIntentHandler {

    fun processNfcIntent(checkIntent: Intent?): NfcTagData? {
        // Check if intent has the action of a discovered NFC tag
        // with NDEF formatted contents
        if (checkIntent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            val rawByteArray = checkIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
            return if (rawByteArray != null) {
                val nfcTagId = extractNfcId(rawByteArray)
                Timber.i("ACTION_TAG_DISCOVERED: found nfcTagId")
                NfcTagData(success = true, tagId = nfcTagId)
            } else {
                Timber.w("ACTION_TAG_DISCOVERED: ID of NFC data is null, NfcTagData can not be used")
                NfcTagData(success = false, tagId = null)
            }
        } else if (checkIntent?.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {

            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val nfcUidByteArray = checkIntent.getByteArrayExtra(NfcAdapter.EXTRA_ID)

            if (rawMessages != null && rawMessages.isNotEmpty() && nfcUidByteArray != null) {
                val ndefMsg = rawMessages[0] as NdefMessage

                val nfcTagId = extractNfcId(nfcUidByteArray)
                return if (ndefMsg.records != null && ndefMsg.records.isNotEmpty()) {
                    val ndefRecord = ndefMsg.records[0]
                    Timber.w("ACTION_NDEF_DISCOVERED: Full NDEF event")
                    NfcTagData(
                        success = true,
                        tagId = nfcTagId,
                        message = String(ndefRecord.payload),
                        type = String(ndefRecord.type)
                    )
                } else {
                    Timber.i("ACTION_NDEF_DISCOVERED: NDEF message is empty, but found nfcTagId")
                    NfcTagData(success = true, tagId = nfcTagId)
                }
            } else {
                Timber.w("ACTION_NDEF_DISCOVERED: ID of NFC data is null, NfcTagData can not be used")
                return NfcTagData(success = false, tagId = null)
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
