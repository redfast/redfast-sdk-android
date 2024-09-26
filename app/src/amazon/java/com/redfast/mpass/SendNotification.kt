package com.redfast.mpass

import android.content.Context
import android.content.Intent
import com.redfast.mpass.notifications.NotificationAction
import com.redfast.mpass.notifications.NotificationConstants
import com.redfast.mpass.notifications.NotificationConstants.ACTION_DEEPLINK
import com.redfast.mpass.notifications.NotificationConstants.ACTION_URL
import com.redfast.mpass.notifications.NotificationsHandler

class SendNotification(context: Context, private val extras: Intent) {

    private val notificationsHandler = NotificationsHandler(context)

    operator fun invoke() {
        val data = extras.extras ?: return
        val action = when {
            !data.getString(ACTION_URL).isNullOrEmpty() -> {
                NotificationAction.OpenUrl(requireNotNull(data.getString(ACTION_URL)))
            }

            !data.getString(ACTION_DEEPLINK).isNullOrEmpty() -> {
                NotificationAction.OpenDeepLink(requireNotNull(data.getString(ACTION_DEEPLINK)))
            }

            else -> NotificationAction.OpenApp
        }
        notificationsHandler.onMessageReceived(
            title = data.getString(NotificationConstants.TITLE_KEY),
            body = data.getString(NotificationConstants.BODY_KEY),
            iconUrl = data.getString(NotificationConstants.ICON_KEY),
            smallIconUrl = data.getString(NotificationConstants.SMALL_ICON_KEY),
            imageUrl = data.getString(NotificationConstants.IMAGE_KEY),
            action = action
        )
    }
}