package org.ossiaustria.amigobox.cloudmessaging

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import org.ossiaustria.amigobox.MainBoxActivity
import org.ossiaustria.lib.domain.modules.UserContext
import timber.log.Timber


class CloudPushHandlerService(val appContext: Context, val userContext: UserContext) {


    fun onNewToken(newToken: String) {
        Timber.tag(TAG).i("New FCM token: $newToken")
    }

    fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(TAG).i("New message received: $message")
        Timber.tag(TAG).i(message.data.toString())

        val intent = Intent(appContext, MainBoxActivity::class.java)
        val type = message.data.get("type") ?: ""
        when (type) {
            "CALL" -> NotificationFactory.createForCall(appContext,
                    intent = intent,
                    title = "Notification title",
                    text = message.data.toString())
            else -> NotificationFactory.createForMessage(
                    appContext,
                    intent = intent,
                    title = "Notification title",
                    text = message.data.toString(),
                    priority = NotificationCompat.PRIORITY_HIGH)
        }

    }

    companion object {
        var instance: CloudPushHandlerService? = null
        const val TAG = "AMIGO-FCM"
    }
}

