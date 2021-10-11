package org.ossiaustria.lib.jitsi.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.koin.android.ext.android.inject
import org.ossiaustria.lib.domain.services.CallService

class AmigoSingleJitsiActivity : JitsiMeetActivity() {

    private val callService: CallService by inject()

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            this@AmigoSingleJitsiActivity.handle(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options = JitsiMeetConferenceOptions.Builder()
            .setRoom("https://meet.jit.si/test123")
            .build()
        join(options)
        setupListeners()
        callService.informJoined()
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
            BroadcastEvent.Type.CONFERENCE_JOINED.action -> callService.informJoined()
            BroadcastEvent.Type.CONFERENCE_TERMINATED.action -> callService.informTerminated()
            BroadcastEvent.Type.CONFERENCE_WILL_JOIN.action -> callService.informWillJoin()
            BroadcastEvent.Type.PARTICIPANT_JOINED.action -> callService.informParticipantJoined()
            BroadcastEvent.Type.PARTICIPANT_LEFT.action -> callService.informParticipantLeft()
        }
    }
}