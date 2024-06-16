package com.fredy.askquestions.features.data.repositoryImpl

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fredy.askquestions.features.data.enums.DisplayMode
import com.fredy.askquestions.features.domain.util.Preferences
import com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel.PreferencesState
import com.fredy.mysavings.Feature.Domain.Repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val context: Context
): PreferencesRepository {

    private val preferences = Preferences(context)

    companion object {
        val DISPLAY_MODE = stringPreferencesKey("display_mode")
        val AUTO_LOGIN = booleanPreferencesKey("auto_login")
        val BIO_AUTH = booleanPreferencesKey("bio_auth")
        val DAILY_NOTIFICATION = booleanPreferencesKey(
            "daily_notification"
        )
        val DAILY_NOTIFICATION_TIME = intPreferencesKey(
            "daily_notification_time"
        )
        val CARRY_ON = booleanPreferencesKey("carry_on")
        val SHOW_TOTAL = booleanPreferencesKey("show_total")
    }

    override fun getDisplayMode(): Flow<DisplayMode> = preferences.getPreference(
        DISPLAY_MODE,
        "System"
    ).map { displayMode ->
            DisplayMode.valueOf(displayMode)
        }

    override suspend fun saveDisplayMode(
        displayMode: DisplayMode
    ) {
        preferences.savePreference(
            DISPLAY_MODE,
            displayMode.name
        )
    }


//    override fun getDailyNotification(): Flow<Boolean> = preferences.getPreference(DAILY_NOTIFICATION, false)
//
//    override suspend fun saveDailyNotification(enableNotification: Boolean) {
//        preferences.savePreference(DAILY_NOTIFICATION, enableNotification)
//    }

    override fun getAutoLogin(): Flow<Boolean> = preferences.getPreference(
        AUTO_LOGIN,
        false
    )

    override suspend fun saveAutoLogin(
        enableAutoLogin: Boolean
    ) {
        preferences.savePreference(
            AUTO_LOGIN,
            enableAutoLogin
        )
    }

    override fun getBioAuth(): Flow<Boolean> = preferences.getPreference(
        BIO_AUTH,
        false
    )

    override suspend fun saveBioAuth(enableBioAuth: Boolean) {
        preferences.savePreference(
            BIO_AUTH,
            enableBioAuth
        )
    }

    override fun getCarryOn(): Flow<Boolean> = preferences.getPreference(
        CARRY_ON,
        false
    )

    override suspend fun saveCarryOn(enableCarryOn: Boolean) {
        preferences.savePreference(
            CARRY_ON,
            enableCarryOn
        )
    }

    override fun getShowTotal(): Flow<Boolean> = preferences.getPreference(
        SHOW_TOTAL,
        false
    )

    override suspend fun saveShowTotal(
        enableShowTotal: Boolean
    ) {
        preferences.savePreference(
            SHOW_TOTAL,
            enableShowTotal
        )
    }

//    override fun getDailyNotificationTime(): Flow<LocalTime> = preferences.getPreference(DAILY_NOTIFICATION_TIME, -1)
//        .map { time ->
//            LocalTimeConverter.intToLocalTime(time)
//        }
//
//    override suspend fun saveDailyNotificationTime(dailyNotificationTime: LocalTime) {
//        preferences.savePreference(DAILY_NOTIFICATION_TIME, LocalTimeConverter.localTimeToInt(dailyNotificationTime))
//    }

    override fun bioAuthStatus(): Boolean {
        val keyGuardManager = context.getSystemService(
            Context.KEYGUARD_SERVICE
        ) as KeyguardManager

        if (!keyGuardManager.isDeviceSecure) {
            return true
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return context.packageManager.hasSystemFeature(
            PackageManager.FEATURE_FINGERPRINT
        )
    }

    override fun getAllPreferenceSettings(): Flow<PreferencesState> = flow {
        val displayMode = getDisplayMode().first()
        val result = PreferencesState(
            displayMode = displayMode,
            autoLogin = getAutoLogin().first(),
            bioAuth = getBioAuth().first(),
//            dailyNotification = getDailyNotification().first(),
//            dailyNotificationTime = getDailyNotificationTime().first(),
            isBioAuthPossible = bioAuthStatus(),
            updated = true
        )
        emit(result)
    }

}
