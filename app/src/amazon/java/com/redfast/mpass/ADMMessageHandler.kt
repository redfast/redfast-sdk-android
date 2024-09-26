package com.redfast.mpass

import android.content.Intent
import android.util.Log
import com.amazon.device.messaging.ADMMessageHandlerBase
import com.redfast.mpass.api.TokenApi
import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.token
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ADMMessageHandler : ADMMessageHandlerBase(this::class.java.name) {

    override fun onMessage(intent: Intent) {
        SendNotification(applicationContext, intent).invoke()
    }

    override fun onRegistrationError(string: String) {
        Log.e(TAG, "ADMMessageHandler:onRegistrationError $string")
    }

    override fun onRegistered(registrationId: String) {
        Log.i(TAG, "ADMMessageHandler:onRegistered")
        DefaultSharedPrefs.token = registrationId
        GlobalScope.launch {
            TokenApi().postToken(registrationId, "ADM")
        }
    }

    override fun onUnregistered(registrationId: String) {
        Log.i(TAG, "ADMMessageHandler:onUnregistered")
    }

    override fun onSubscribe(topic: String) {
        Log.i(TAG, "onSubscribe: $topic")
    }

    override fun onSubscribeError(topic: String, errorId: String) {
        Log.i(TAG, "onSubscribeError: errorId: $errorId topic: $topic")
    }

    override fun onUnsubscribe(topic: String) {
        Log.i(TAG, "onUnsubscribe: $topic")
    }

    override fun onUnsubscribeError(topic: String, errorId: String) {
        Log.i(TAG, "onUnsubscribeError: errorId: $errorId topic: $topic")
    }

    companion object {
        private const val TAG = "ADMMessageHandler"
    }
}
