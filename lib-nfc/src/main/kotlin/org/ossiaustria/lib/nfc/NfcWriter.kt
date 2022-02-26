package org.ossiaustria.lib.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import timber.log.Timber
import java.io.IOException

class NfcWriter {

    fun createNfcMessage(payload: String, intent: Intent?): Boolean {

        val pathPrefix = NfcConstants.PREFIX
        // val pathPrefix = ""
        val nfcRecord = NdefRecord(
            NdefRecord.TNF_EXTERNAL_TYPE,
            pathPrefix.toByteArray(),
            ByteArray(0),
            payload.toByteArray()
        )

        // val nfcRecord = NdefRecord.createMime("text/plain", payload.toByteArray())
        val nfcMessage = NdefMessage(arrayOf(nfcRecord))
        intent?.let {
            val tag = it.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            return writeMessageToTag(nfcMessage, tag)
        }
        return false
    }

    private fun writeMessageToTag(nfcMessage: NdefMessage, tag: Tag?): Boolean {

        try {
            val nDefTag = Ndef.get(tag)

            nDefTag?.let {
                it.connect()
                if (it.maxSize < nfcMessage.toByteArray().size) {
                    // Message to large to write to NFC tag
                    return false
                }
                return if (it.isWritable) {
                    it.writeNdefMessage(nfcMessage)
                    it.close()
                    // Message is written to tag
                    true
                } else {
                    // NFC tag is read-only
                    false
                }
            }

            val nDefFormatableTag = NdefFormatable.get(tag)

            nDefFormatableTag?.let {
                try {
                    it.connect()
                    it.format(nfcMessage)
                    it.close()
                    // The data is written to the tag
                    return true
                } catch (e: IOException) {
                    // Failed to format tag
                    Timber.e(e)

                    return false
                }
            }
            // NDEF is not supported
            return false
        } catch (e: Exception) {
            Timber.e(e)
        }
        return false
    }
}
