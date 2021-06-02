package org.ossiaustria.lib.nfc

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class NfcHandlerTest {

    lateinit var subject: NfcHandler

    @Before
    fun beforeEach() {
        subject = NfcHandler()
    }

    @Test
    fun `processNfcIntent should return null when no intent was given`() {
        val result = subject.processNfcIntent(null)

        assertNull(result)
    }

    @Test
    fun `processNfcIntent should return null for intent without ACTION_NDEF_DISCOVERED`() {
        val mockIntent = mockk<Intent>()
        every { mockIntent.action } returns "another_action"

        val result = subject.processNfcIntent(mockIntent)

        assertNull(result)
    }

    @Test
    fun `processNfcIntent should return NfcInfo for intent with ACTION_NDEF_DISCOVERED`() {
        val mockIntent = mockk<Intent>()
        every { mockIntent.action } returns NfcAdapter.ACTION_NDEF_DISCOVERED
        every { mockIntent.getParcelableArrayExtra(any()) } returns null
        every { mockIntent.getByteArrayExtra(any()) } returns listOf(16.toByte()).toByteArray()
        mockNdefMessages(mockIntent)

        val result = subject.processNfcIntent(mockIntent)

        assertNotNull(result)
    }

    @Test
    fun `processNfcIntent should return valid NfcInfo with EXTRA_NDEF_MESSAGES `() {
        val mockIntent = mockk<Intent>()
        every { mockIntent.action } returns NfcAdapter.ACTION_NDEF_DISCOVERED

        every { mockIntent.getByteArrayExtra(any()) } returns listOf(16.toByte()).toByteArray()
        mockNdefMessages(mockIntent)

        val result = subject.processNfcIntent(mockIntent)

        assertNotNull(result)
        assertEquals("data", result?.message)
    }

    @Test
    fun `processNfcIntent should return NfcInfo with EXTRA_ID in hex format`() {
        val mockIntent = mockk<Intent>()
        every { mockIntent.action } returns NfcAdapter.ACTION_NDEF_DISCOVERED

        every { mockIntent.getParcelableArrayExtra(any()) } returns null

        mockNdefMessages(mockIntent)

        every { mockIntent.getByteArrayExtra(any()) } returns null
        every { mockIntent.getByteArrayExtra(eq(NfcAdapter.EXTRA_ID)) } returns
            listOf(16.toByte()).toByteArray()

        val result = subject.processNfcIntent(mockIntent)

        assertNotNull(result)
        // x10 = 16
        assertEquals("10", result?.tagId)
    }

    private fun mockNdefMessages(mockIntent: Intent) {
        val message = mockk<NdefMessage>()
        val record = mockk<NdefRecord>()
        every { message.records } returns listOf(record).toTypedArray()
        every { record.payload } returns "data".toByteArray()
        every { record.type } returns "type".toByteArray()

        every { mockIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) } returns arrayOf(
            message
        )
    }
}
