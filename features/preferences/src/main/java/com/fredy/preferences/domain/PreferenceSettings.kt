package com.fredy.preferences.domain

import com.fredy.preferences.data.DisplayMode
import java.time.LocalTime


data class PreferenceSettings(
    val displayMode: DisplayMode = DisplayMode.System,
    val autoLogin: Boolean = false,
    val bioAuth: Boolean = false,
    val isBioAuthPossible: Boolean = false,
    val dailyNotification: Boolean = false,
    val dailyNotificationTime: LocalTime = LocalTime.now(),
    val updated: Boolean = false,
)