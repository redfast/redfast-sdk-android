package com.redfast.mpass

import com.amazon.device.messaging.ADMMessageReceiver

class ADMMessageReceiver : ADMMessageReceiver(ADMMessageHandler::class.java) {
    init {
        if (isAdmv2) {
            registerJobServiceClass(ADMMessageHandlerJobBase::class.java, JOB_ID)
        }
    }
}
