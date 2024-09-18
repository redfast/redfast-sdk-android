package com.redfast.mpass

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.redfast.mpass.api.TokenApi
import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.token
import com.redfast.mpass.notifications.AppMessaging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class MessagingProvider(
    private val context: Context,
    private val messaging: FirebaseMessaging = Firebase.messaging
) : AppMessaging {

    override fun autoInit(init: Boolean) {
        messaging.isAutoInitEnabled = init
    }

    override suspend fun getToken(): String? {
        val task: Task<String> = suspendCancellableCoroutine { continuation ->
            messaging.token.addOnCompleteListener { task ->
                if (continuation.isActive) {
                    continuation.resume(task)
                }
            }
        }
        if (!task.isSuccessful) {
            Log.d(this::class.simpleName, "Fetching FCM token failed")
            return null
        }
        val token = task.result
        DefaultSharedPrefs.token = token
        GlobalScope.launch {
            TokenApi().postToken(token, "GCM")
        }
        return token
    }
}