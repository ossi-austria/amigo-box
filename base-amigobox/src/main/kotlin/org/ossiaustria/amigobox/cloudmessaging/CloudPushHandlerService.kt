package org.ossiaustria.amigobox.cloudmessaging

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import org.ossiaustria.amigobox.MainBoxActivity
import org.ossiaustria.amigobox.Navigator
import org.ossiaustria.amigobox.R
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.services.events.AmigoCloudEvent
import org.ossiaustria.lib.domain.services.events.AmigoCloudEventType
import org.ossiaustria.lib.domain.services.events.IncomingEventCallbackService
import timber.log.Timber

class CloudPushHandlerService(
    private val appContext: Context,
    private val incomingEventCallbackService: IncomingEventCallbackService,
    private val entryPointProvider: IntentEntryPointProvider,
) {
    private var mainBoxActivity: MainBoxActivity? = null
    fun onNewToken(newToken: String) {
        Timber.tag(TAG).i("New FCM token: $newToken")
    }

    fun bindToActivity(activity: MainBoxActivity) {
        mainBoxActivity = activity
    }

    fun unbind() {
        mainBoxActivity = null
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
                val handled = incomingEventCallbackService.handleCloudEventCall(cloudEvent)
                if (!handled) {
                    val callResource = incomingEventCallbackService.handleCall(cloudEvent.entityId)
                    if (callResource is Resource.Success) {
                        if (!callResource.value.isDone()) {
                            startActivityForCall(appContext, callResource.value)
                        }
                    }
                }
            } else {
                // TODO: need improvements, could look similar to CALL
                val clazz = entryPointProvider.getMainClass()
                val intent = Intent(appContext, clazz)
                createMessage(intent, cloudEvent)
            }
        }
    }

    private fun startActivityForCall(appContext: Context, call: Call) {
        Timber.tag(TAG).i("startActivityForCall: $call, $mainBoxActivity")

        val clazz = entryPointProvider.getMainClass()
        val intent = if (mainBoxActivity != null) {
            Intent(mainBoxActivity, clazz)
        } else {
            Intent(appContext, clazz).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtras(Bundle().apply { Navigator.setCall(this, call) })
        (mainBoxActivity ?: appContext).startActivity(intent)
    }

    private fun createMessage(
        intent: Intent,
        cloudEvent: AmigoCloudEvent,
    ) {
        Timber.i("createMessage: $cloudEvent")
        NotificationFactory.createForMessage(
            appContext,
            intent = intent,
            title = appContext.getString(R.string.notification_new_message),
            text = appContext.getString(R.string.notification_new_message),
            priority = NotificationCompat.PRIORITY_HIGH
        )
    }

    companion object {
        var instance: CloudPushHandlerService? = null
        const val TAG = "AMIGO-FCM"
    }
}

