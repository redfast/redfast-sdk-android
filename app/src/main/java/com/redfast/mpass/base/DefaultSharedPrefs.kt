package com.redfast.mpass.base

import android.content.Context
import android.content.SharedPreferences
import com.redfast.mpass.sharedApplicationContext
import androidx.core.content.edit


private const val PREF_NAME = "REDFAST_DEFAULT_SHARED_PREFS"

object DefaultSharedPrefs : SharedPreferences by sharedApplicationContext.getSharedPreferences(
    PREF_NAME, Context.MODE_PRIVATE
)

private const val TOKEN_KEY = "FCM_TOKEN_KEY"
private const val USER_ID = "USER_ID"

var SharedPreferences.token: String
    get() = getString(TOKEN_KEY, "").orEmpty()
    set(value) = edit {
        putString(TOKEN_KEY, value)
    }

private const val DEFAULT_USER_ID = "DEFAULT_USER_ID"
var SharedPreferences.userId: String
    get() = getString(USER_ID, DEFAULT_USER_ID) ?: DEFAULT_USER_ID
    set(value) = edit {
        putString(USER_ID, value.ifEmpty { null })
    }