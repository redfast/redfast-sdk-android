package com.redfast.mpass


private const val ADM_CLASSNAME = "com.amazon.device.messaging.ADM"
private const val ADMV2_HANDLER = "com.amazon.device.messaging.ADMMessageHandlerJobBase"
const val JOB_ID = 1324124

val isAdmAvailable = ADM_CLASSNAME.isClassAvailable()
val isAdmv2 = if (isAdmAvailable) ADMV2_HANDLER.isClassAvailable() else false

private fun String.isClassAvailable(): Boolean {
    return try {
        Class.forName(this)
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}
