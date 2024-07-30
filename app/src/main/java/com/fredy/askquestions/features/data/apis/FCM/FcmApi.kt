package com.fredy.askquestions.features.data.apis.FCM

import com.fredy.askquestions.features.data.apis.FCM.dto.SendMessage
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("/send")
    suspend fun sendMessage(
        @Body body: SendMessage
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body: SendMessage
    )
}