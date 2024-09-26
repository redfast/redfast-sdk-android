package com.redfast.mpass.notifications

sealed class NotificationAction {
    data object OpenApp : NotificationAction()
    data class OpenUrl(val url: String) : NotificationAction()
    data class OpenDeepLink(val deeplink: String) : NotificationAction()
}