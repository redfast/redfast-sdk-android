package com.redfast.mpass

import android.content.Context
import com.amazon.device.messaging.ADM
import com.redfast.mpass.api.TokenApi
import com.redfast.mpass.base.DefaultSharedPrefs
import com.redfast.mpass.base.token
import com.redfast.mpass.notifications.AppMessaging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MessagingProvider(private val context: Context) : AppMessaging {

    override fun autoInit(init: Boolean) {
        val adm = ADM(context)
        if (adm.registrationId == null) {
            adm.startRegister()
        } else {
            DefaultSharedPrefs.token = adm.registrationId
            GlobalScope.launch {
                TokenApi().postToken(adm.registrationId, "ADM")
            }
        }
    }

    override suspend fun getToken(): String? {
        return null
    }


}