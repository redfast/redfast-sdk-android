package com.redfast.mpass.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.redfast.mpass.MainActivity
import com.redfast.mpass.R
import com.redfast.mpass.notifications.NotificationConstants.DEEP_LINK_IN_APP
import com.redfast.mpass.notifications.NotificationConstants.DEEP_LINK_PROMPT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificationsHandler(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "com.redfast.mpass.notifications.ANDROID"
        private const val CHANNEL_NAME = "Redfast Notifications"
        const val SCREEN_NAME_KEY = "SCREEN_NAME_KEY"
        const val PROMPT_ID_KEY = "PROMPT_ID_KEY"
        const val SKU_ID_KEY = "SKU_ID_KEY"
    }

    fun onMessageReceived(
        title: String?,
        body: String?,
        iconUrl: String?,
        smallIconUrl: String?,
        imageUrl: String?,
        action: NotificationAction = NotificationAction.OpenApp
    ) {
        if (title.isNullOrEmpty() && body.isNullOrEmpty()) {
            return
        }
        if (!iconUrl.isNullOrEmpty() || !smallIconUrl.isNullOrEmpty() || !imageUrl.isNullOrEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val smallIconBitmap =
                    if (smallIconUrl.isNullOrEmpty()) null else getBitmap(smallIconUrl)
                val iconBitmap =
                    if (iconUrl.isNullOrEmpty()) null else getBitmap(iconUrl)
                val imageBitmap =
                    if (imageUrl.isNullOrEmpty()) null else getBitmap(imageUrl)
                withContext(Dispatchers.Main) {
                    val notificationBuilder = getNotificationBuilder(
                        title,
                        body,
                        smallIconBitmap,
                        iconBitmap,
                        imageBitmap
                    )
                    showNotification(notificationBuilder, action)
                }
            }
        } else {
            showNotification(getNotificationBuilder(title, body), action)
        }
    }

    private fun getBitmap(url:String) : Bitmap {
       return Glide.with(context).asBitmap().load(url).submit().get()
    }

    private fun getNotificationBuilder(
        title: String?,
        body: String?,
        smallIconBitmap: Bitmap?,
        iconBitmap: Bitmap?,
        image: Bitmap?
    ): Notification.Builder {
        val builder = Notification.Builder(context, CHANNEL_ID)
            .setLargeIcon(iconBitmap)
            .setColor(context.getColor(R.color.colorPrimary))
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (smallIconBitmap != null) {
            builder.setSmallIcon(Icon.createWithBitmap(smallIconBitmap))
            builder.setColor(context.resources.getColor(android.R.color.transparent, null))
        } else {
            builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        }
        if (image != null) {
            builder.setStyle(Notification.BigPictureStyle().bigPicture(image))
        }
        return builder
    }

    private fun getNotificationBuilder(title: String?, body: String?): Notification.Builder {
        return Notification.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setColor(context.getColor(R.color.colorPrimary))
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
    }

    private fun showNotification(builder: Notification.Builder, action: NotificationAction?) {
        val intent = when (action) {
            is NotificationAction.OpenUrl -> openUrl(action.url)
            is NotificationAction.OpenDeepLink -> openApp(action.deeplink)
            else -> openApp()
        }

        val id = SystemClock.uptimeMillis().toInt()
        val pIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        val managerCompat = NotificationManagerCompat.from(context)
        createNotificationChannel(managerCompat)
        managerCompat.notify(id, builder.setContentIntent(pIntent).build())
    }

    private fun openUrl(url: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        return intent
    }

    private fun openApp(deeplink: String? = null): Intent {
        val bundle = Bundle()
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        when {
            deeplink.isNullOrEmpty() -> {/*nothing here*/ }
            deeplink.contains(DEEP_LINK_PROMPT) -> {
                val promptId = deeplink.substringAfter("redflix://$DEEP_LINK_PROMPT/")
                bundle.putString(PROMPT_ID_KEY, promptId)
            }
            deeplink.contains(DEEP_LINK_IN_APP) -> {
                val inApp = deeplink.substringAfter("redflix://$DEEP_LINK_IN_APP/")
                bundle.putString(SKU_ID_KEY, inApp)
            }
            else -> {
                val screenName = deeplink.substringAfter("redflix://")
                bundle.putString(SCREEN_NAME_KEY, screenName)
            }
        }

        intent.putExtras(bundle)
        return intent
    }

    private fun createNotificationChannel(managerCompat: NotificationManagerCompat) {
        if (managerCompat.getNotificationChannel(CHANNEL_ID) != null) {
            return
        }
        val newChannel =
            NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH)
                .setName(CHANNEL_NAME)
                .setVibrationEnabled(true)
                .setLightsEnabled(true)
                .setShowBadge(true)
                .build()
        managerCompat.createNotificationChannel(newChannel)
    }
}