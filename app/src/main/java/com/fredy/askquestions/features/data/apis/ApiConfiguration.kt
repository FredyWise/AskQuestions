package com.fredy.askquestions.features.data.apis

import com.fredy.askquestions.BuildConfig


sealed class ApiConfiguration {
    object GeminiModel{
        const val API_KEY = BuildConfig.GEMINI_API_KEY
        const val FLASH_MODEL_1_5 = "gemini-1.5-flash"
    }

    object FirebaseModel{
        const val USER_ENTITY = "user"
        const val CHAT_ENTITY = "chat"
        const val MESSAGE_ENTITY = "message"
    }

    object WebClient{
        const val ID = BuildConfig.WEB_CLIENT_ID
    }

    object AppData{
        const val AppVersion = BuildConfig.VERSION_NAME
    }

}
