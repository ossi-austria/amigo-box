package org.ossiaustria.amigobox.cloudmessaging

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

object FCMHelper {
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
                    callback(token)
                }
            })
    }

    fun tokenSuccessCallback(context: Context, token: String?) {
        val msg = "new token: $token"
        Timber.d(msg)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}