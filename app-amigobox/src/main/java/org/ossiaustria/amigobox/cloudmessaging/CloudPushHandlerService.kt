package org.ossiaustria.amigobox.cloudmessaging

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import org.ossiaustria.amigobox.MainBoxActivity
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.services.AmigoCloudEvent
import org.ossiaustria.lib.domain.services.AmigoCloudEventType
import org.ossiaustria.lib.domain.services.IncomingEventCallbackService
import timber.log.Timber

class CloudPushHandlerService(
    private val appContext: Context,
    private val incomingEventCallbackService: IncomingEventCallbackService,
) {

    fun onNewToken(newToken: String) {
        Timber.tag(TAG).i("New FCM token: $newToken")
    }

    /**
     * Transform FCM Data Message (important: HAS to be a Data Message),
     * into an "AmigoCloudEvent" and emits the Event to incomingEventsViewModel OR starts the AmigoBox activity
     */
    fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(TAG).i("New message received: $message")
        Timber.tag(TAG).i(message.data.toString())

        val cloudEvent = AmigoCloudEvent.fromMap(message.data)
        if (cloudEvent != null) {
            if (cloudEvent.type == AmigoCloudEventType.CALL) {
                incomingEventCallbackService.handleCloudEventCall(cloudEvent) {
                    startActivityForCall(appContext, it)
                }
            } else {
                // TODO: need improvements, could look similar to CALL
                val intent = Intent(appContext, MainBoxActivity::class.java)
                createMessage(intent, cloudEvent)
            }
        }
    }

    private fun startActivityForCall(appContext: Context, call: Call) {
        Timber.tag(TAG).i("startActivityForCall: $call")
        val intent = Intent(appContext, MainBoxActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtras(Bundle().apply { Navigator.setCall(this, call) })
        appContext.startActivity(intent)
    }

    private fun createMessage(
        intent: Intent,
        cloudEvent: AmigoCloudEvent,
    ) {
        Timber.i("createMessage: $cloudEvent")
        NotificationFactory.createForMessage(
            appContext,
            intent = intent,
            title = "Du hast eine neue Nachricht bekommen!",
            text = "Du hast eine neue Nachricht bekommen!",
            priority = NotificationCompat.PRIORITY_HIGH
        )
    }

    companion object {
        var instance: CloudPushHandlerService? = null
        const val TAG = "AMIGO-FCM"
    }
}

