package org.ossiaustria.amigobox.cloudmessaging

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber


class DefaultFirebaseMessagingService() : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        Timber.tag(TAG).i("New FCM token: $newToken")
        if (CloudPushHandlerService.instance != null) {
            CloudPushHandlerService.instance?.onNewToken(newToken)
        } else {
            Timber.tag(TAG).w("CloudPushHandlerService.instance == null !")
            super.onNewToken(newToken)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Timber.tag(TAG).i("New message received: $message")
        if (CloudPushHandlerService.instance != null) {
            CloudPushHandlerService.instance?.onMessageReceived(message)
        } else {
            Timber.tag(TAG).w("CloudPushHandlerService.instance == null !")
            super.onMessageReceived(message)
        }
    }

    companion object {
        const val TAG = "FCM"
    }
}

