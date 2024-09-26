package com.redfast.mpass

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.redfast.mpass.api.TokenApi
import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.token
import com.redfast.mpass.notifications.NotificationAction
import com.redfast.mpass.notifications.NotificationConstants.BODY_KEY
import com.redfast.mpass.notifications.NotificationConstants.ICON_KEY
import com.redfast.mpass.notifications.NotificationConstants.IMAGE_KEY
import com.redfast.mpass.notifications.NotificationConstants.SMALL_ICON_KEY
import com.redfast.mpass.notifications.NotificationConstants.TITLE_KEY
import com.redfast.mpass.notifications.NotificationConstants.ACTION_URL
import com.redfast.mpass.notifications.NotificationConstants.ACTION_DEEPLINK
import com.redfast.mpass.notifications.NotificationsHandler
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirebaseAppMessagingService : FirebaseMessagingService() {

    private val handler = NotificationsHandler(this)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onNewToken(token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            DefaultSharedPrefs.token = token
            TokenApi().postToken(token, "GCM")
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val action = when {
            !data[ACTION_URL].isNullOrEmpty() -> {
                NotificationAction.OpenUrl(requireNotNull(data[ACTION_URL]))
            }

            !data[ACTION_DEEPLINK].isNullOrEmpty() -> {
                NotificationAction.OpenDeepLink(requireNotNull(data[ACTION_DEEPLINK]))
            }

            else -> NotificationAction.OpenApp
        }

        handler.onMessageReceived(
            title = data[TITLE_KEY] ?: message.notification?.title,
            body = data[BODY_KEY] ?: message.notification?.body,
            iconUrl = data[ICON_KEY],
            smallIconUrl = data[SMALL_ICON_KEY],
            imageUrl = data[IMAGE_KEY],
            action = action
        )
    }
}