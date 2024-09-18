package com.redfast.mpass.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redfast.mpass.MessagingProvider
import com.redfast.mpass.notifications.AppMessaging
import com.redfast.mpass.notifications.MessagingPermissionRequester

abstract class BaseActivity : AppCompatActivity() {

    private val messaging: AppMessaging = MessagingProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        messaging.autoInit(true)
        MessagingPermissionRequester(this).request()
    }
}