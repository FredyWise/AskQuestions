package com.fredy.core.data.Util

import com.fredy.core.BuildConfig


sealed class Configuration {
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

    object Debug{
        const val DEBUG = true
    }

    object UIConfig{
        const val MessageLimit = 20
    }

}
