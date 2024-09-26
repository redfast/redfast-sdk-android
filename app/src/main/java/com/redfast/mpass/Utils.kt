package com.redfast.mpass

import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics

object Utils {
    fun dp2px(dp: Int): Int {
        val metrics = Resources.getSystem().displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()
    }

    fun isTV(): Boolean {
        return (Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_TYPE_TELEVISION) != 0
    }

    fun isTablet(): Boolean {
        val displayMetrics = Resources.getSystem().displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val wi = width.toDouble() / displayMetrics.xdpi.toDouble()
        val hi = height.toDouble() / displayMetrics.ydpi.toDouble()
        val x = Math.pow(wi, 2.0)
        val y = Math.pow(hi, 2.0)
        val screenInches = Math.sqrt(x + y)
        return screenInches >= 7
    }

    fun isLandscape(): Boolean {
        return Resources.getSystem().configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    enum class DeviceType {
        phone, tablet, tv
    }

    fun getDeviceType(): DeviceType {
        if (isTV())
            return DeviceType.tv
        if (isTablet())
            return DeviceType.tablet
        return DeviceType.phone
    }
}