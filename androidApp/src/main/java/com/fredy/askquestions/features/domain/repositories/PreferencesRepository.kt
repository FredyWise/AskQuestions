package com.fredy.mysavings.Feature.Domain.Repository

import androidx.compose.ui.graphics.Color
import com.fredy.askquestions.features.data.enums.DisplayMode
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesState
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface PreferencesRepository {

    // Display mode
    fun getDisplayMode(): Flow<DisplayMode>
    suspend fun saveDisplayMode(displayMode: DisplayMode)

    // Notifications
//    fun getDailyNotification(): Flow<Boolean>
//    suspend fun saveDailyNotification(
//        enableNotification: Boolean
//    )

    // Login
    fun getAutoLogin(): Flow<Boolean>
    suspend fun saveAutoLogin(enableAutoLogin: Boolean)

    // Authentication
    fun getBioAuth(): Flow<Boolean>
    suspend fun saveBioAuth(enableBioAuth: Boolean)
//    fun getDailyNotificationTime(): Flow<LocalTime>
//    suspend fun saveDailyNotificationTime(
//        dailyNotificationTime: LocalTime
//    )

    fun bioAuthStatus(): Boolean

    // View
    fun getCarryOn(): Flow<Boolean>
    suspend fun saveCarryOn(enableCarryOn: Boolean)
    fun getShowTotal(): Flow<Boolean>
    suspend fun saveShowTotal(enableShowTotal: Boolean)

    // ALL
    fun getAllPreferenceSettings(): Flow<PreferencesState>
}

