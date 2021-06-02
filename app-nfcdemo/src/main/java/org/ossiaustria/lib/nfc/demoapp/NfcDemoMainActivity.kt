package org.ossiaustria.lib.nfc.demoapp

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.ossiaustria.lib.nfc.NfcConstants
import org.ossiaustria.lib.nfc.NfcWriter
import timber.log.Timber
import java.io.IOException


@AndroidEntryPoint
class NfcDemoMainActivity : AppCompatActivity() {

    private lateinit var editTextNfc: EditText
    private lateinit var buttonNfc: Button

    private lateinit var nfcAdapter: NfcAdapter
    private val nfcWriter: NfcWriter = NfcWriter()

    private var nfcPendingIntent: PendingIntent? = null

    //inserting ID to write to tag
    private var textNfc: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )

        editTextNfc = findViewById(R.id.nfc_message_input)
        buttonNfc = findViewById(R.id.add_button)

    }

    override fun onResume() {
        super.onResume()
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, null, null)

        buttonNfc.setOnClickListener {
            textNfc = editTextNfc.text.toString()
            val message = if (textNfc.isEmpty()) {
                "the ID you entered is empty"
            } else {
                "ID is ready to be written to NFC-Tag"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onPause() {
        super.onPause()

        nfcAdapter.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (textNfc.isNotEmpty()) {
            val messageWrittenSuccessfully = nfcWriter.createNfcMessage(textNfc, intent)
            if (messageWrittenSuccessfully) {
                Toast.makeText(this, "Successfully Written to Tag", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Something When wrong Try Again", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "You forgot to enter an ID", Toast.LENGTH_SHORT).show()
        }

    }





}