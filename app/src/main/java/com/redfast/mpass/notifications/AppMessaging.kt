package com.redfast.mpass.notifications

interface AppMessaging {
    fun autoInit(init: Boolean)
    suspend fun getToken(): String?
}