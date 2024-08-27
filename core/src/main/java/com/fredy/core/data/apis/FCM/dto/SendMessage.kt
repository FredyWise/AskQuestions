package com.fredy.core.data.apis.FCM.dto

import com.fredy.core.data.apis.FCM.dto.NotificationBody

data class SendMessage (
    val to: String?,
    val notification: NotificationBody
)