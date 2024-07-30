package com.fredy.askquestions.features.data.apis.FCM.dto

data class SendMessage (
    val to: String?,
    val notification: NotificationBody
)