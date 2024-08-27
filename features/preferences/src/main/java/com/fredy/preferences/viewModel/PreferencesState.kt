package com.fredy.preferences.viewModel


import java.time.LocalDateTime
import java.time.LocalTime

data class PreferencesState(
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now(),
//    val displayMode: DisplayMode = DisplayMode.System,
    val isDarkMode: Boolean? = false,
    val autoLogin: Boolean = false,
    val bioAuth: Boolean = false,
    val isBioAuthPossible: Boolean = false,
    val isShowColorPallet: Boolean = false,
    val dailyNotification: Boolean = false,
    val dailyNotificationTime: LocalTime = LocalTime.now(),
    val updated: Boolean = false,
)