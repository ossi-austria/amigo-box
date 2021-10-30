package org.ossiaustria.lib.jitsi.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.koin.android.ext.android.inject
import org.ossiaustria.lib.domain.services.IncomingEventCallbackService
import timber.log.Timber
import java.net.URL

class AmigoSingleJitsiActivity : JitsiMeetActivity() {

    private val incomingEventCallbackService: IncomingEventCallbackService by inject()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            this@AmigoSingleJitsiActivity.handle(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jitsiUrl = intent.getStringExtra(PARAM_JITSI_URL)
        val jitsiToken = intent.getStringExtra(PARAM_JITSI_TOKEN)
        if (jitsiUrl.isNullOrBlank() && jitsiToken.isNullOrBlank()) {
            Toast.makeText(this, "jitsiUrl:$jitsiUrl jitsiToken:$jitsiToken", Toast.LENGTH_SHORT)
                .show()
        } else {
            val url = URL("https://amigo-dev.ossi-austria.org")
            val options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(url)
                .setRoom(jitsiUrl)
                .setToken(jitsiToken)
                .build()
            Timber.i("Connecting to $options")
            join(options)
            setupListeners()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }

    private fun setupListeners() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_JOINED.action)
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.action)
        intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_WILL_JOIN.action)
        intentFilter.addAction(BroadcastEvent.Type.PARTICIPANT_JOINED.action)
        intentFilter.addAction(BroadcastEvent.Type.PARTICIPANT_LEFT.action)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }

    private fun handle(intent: Intent) {
        when (intent.action) {
            BroadcastEvent.Type.CONFERENCE_JOINED.action -> incomingEventCallbackService.informJoined()
            BroadcastEvent.Type.CONFERENCE_TERMINATED.action -> incomingEventCallbackService.informTerminated()
            BroadcastEvent.Type.CONFERENCE_WILL_JOIN.action -> incomingEventCallbackService.informWillJoin()
            BroadcastEvent.Type.PARTICIPANT_JOINED.action -> incomingEventCallbackService.informParticipantJoined()
            BroadcastEvent.Type.PARTICIPANT_LEFT.action -> incomingEventCallbackService.informParticipantLeft()
        }
    }

    companion object {
        const val PARAM_JITSI_URL = "JITSI_URL"
        const val PARAM_JITSI_TOKEN = "JITSI_TOKEN"
    }
}