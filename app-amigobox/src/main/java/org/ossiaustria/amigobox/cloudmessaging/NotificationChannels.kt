package org.ossiaustria.amigobox.cloudmessaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import org.ossiaustria.amigobox.R
import timber.log.Timber

object NotificationChannels {

    fun messageChannelId(context: Context) = context.getString(R.string.channel_message_id)
    fun infoChannelId(context: Context) = context.getString(R.string.channel_events_id)

    fun createAllChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(
                context,
                R.string.channel_message_id,
                R.string.channel_message_name,
                R.string.channel_message_description,
                NotificationManager.IMPORTANCE_HIGH
            )

            createNotificationChannel(
                context,
                R.string.channel_events_id,
                R.string.channel_events_name,
                R.string.channel_events_description,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        context: Context,
        @StringRes idRes: Int,
        @StringRes nameRes: Int,
        @StringRes descriptionTextRes: Int,
        importance: Int
    ) {

        val id = context.getString(idRes)
        val name = context.getString(nameRes)
        val descriptionText = context.getString(descriptionTextRes)

        Timber.i("Create NotificationChannel: $id '$name' $importance")
        val channel = NotificationChannel(id, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}