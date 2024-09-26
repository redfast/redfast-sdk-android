package com.redfast.mpass

import android.content.Context
import android.content.Intent
import android.util.Log
import com.amazon.device.messaging.ADMMessageHandlerJobBase
import com.redfast.mpass.api.TokenApi
import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.token
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ADMMessageHandlerJobBase : ADMMessageHandlerJobBase() {

    override fun onMessage(context: Context, intent: Intent) {
        SendNotification(context, intent).invoke()
    }

    override fun onRegistrationError(context: Context, string: String) {
        Log.e(TAG, "SampleADMMessageHandlerJobBase:onRegistrationError $string")
    }

    override fun onRegistered(context: Context, registrationId: String) {
        Log.i(TAG, "SampleADMMessageHandlerJobBase:onRegistered")
        DefaultSharedPrefs.token = registrationId
        GlobalScope.launch {
            TokenApi().postToken(registrationId, "ADM")
        }
    }

    override fun onUnregistered(context: Context, registrationId: String) {
        Log.i(TAG, "SampleADMMessageHandlerJobBase:onUnregistered")
    }

    override fun onSubscribe(context: Context, topic: String) {
        Log.i(TAG, "onSubscribe: $topic")
    }

    override fun onSubscribeError(context: Context, topic: String, errorId: String) {
        Log.i(TAG, "onSubscribeError: errorId: $errorId topic: $topic")
    }

    override fun onUnsubscribe(context: Context, topic: String) {
        Log.i(TAG, "onUnsubscribe: $topic")
    }

    override fun onUnsubscribeError(context: Context, topic: String, errorId: String) {
        Log.i(TAG, "onUnsubscribeError: errorId: $errorId topic: $topic")
    }

    companion object {
        private const val TAG = "ADMJobBase"
    }
}
