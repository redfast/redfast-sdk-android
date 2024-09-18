package com.redfast.mpass.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

class MessagingPermissionRequester(private val activity: FragmentActivity) : PermissionRequester {

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val message = if (isGranted) "is" else "is NOT"
        Log.d(this::class.java.simpleName, "Notification permission $message granted")
    }

    override fun request() {
        if (activity.isDestroyed) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(this::class.java.simpleName, "Notification permission already granted")
            } else if (activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                val contextView = activity.findViewById<View>(android.R.id.content)
                val action = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }
                Snackbar.make(
                    contextView,
                    "Grant permission otherwise you won't receive notifications",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("OK") { action.invoke() }
                    .show()
            } else {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


}