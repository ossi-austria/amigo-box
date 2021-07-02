package org.ossiaustria.amigobox.cloudmessaging

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.ossiaustria.lib.domain.services.AuthService
import timber.log.Timber

class FCMHelper(private val authService: AuthService) {

    fun getToken(callback: ((String?) -> Unit)) {

        FirebaseMessaging.getInstance()
            .token
            .addOnCompleteListener(object : OnCompleteListener<String?> {
                override fun onComplete(task: Task<String?>) {
                    if (!task.isSuccessful) {
                        Timber.w(task.exception, "Fetching FCM registration token failed")
                        return
                    }

                    // Get new FCM registration token
                    val token: String? = task.result
                    runBlocking {
                        tokenSuccessCallback(token)
                    }
                    callback(token)
                }
            })
    }

    suspend fun tokenSuccessCallback(token: String?) {
        val msg = "new token: $token"
        if (token != null) {
            authService.setFcmToken(token).last()
        }
        Timber.d(msg)
    }
}