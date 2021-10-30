package org.ossiaustria.amigobox.cloudmessaging

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import org.ossiaustria.amigobox.MainBoxActivity
import org.ossiaustria.lib.domain.services.AmigoCloudEvent
import org.ossiaustria.lib.domain.services.IncomingEventCallbackService
import timber.log.Timber

class CloudPushHandlerService(
    private val appContext: Context,
    private val incomingEventCallbackService: IncomingEventCallbackService,
) {

    fun onNewToken(newToken: String) {
        Timber.tag(TAG).i("New FCM token: $newToken")
    }

    fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(TAG).i("New message received: $message")
        Timber.tag(TAG).i(message.data.toString())

        val intent = Intent(appContext, MainBoxActivity::class.java)
        val type = message.data.get("type") ?: ""

        val cloudEvent = AmigoCloudEvent.fromMap(message.data)
        val handled = if (cloudEvent != null)
            incomingEventCallbackService.handleEvent(cloudEvent)
        else false

        if (!handled) when (type) {
            "call" -> NotificationFactory.createForCall(
                appContext,
                intent = intent,
                title = "Incoming CALL!",
                text = message.data.toString()
            )
            else -> NotificationFactory.createForMessage(
                appContext,
                intent = intent,
                title = "Message retrieved",
                text = message.data.toString(),
                priority = NotificationCompat.PRIORITY_HIGH
            )
        }

    }

    companion object {
        var instance: CloudPushHandlerService? = null
        const val TAG = "AMIGO-FCM"
    }
}

