package com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel

import androidx.compose.ui.graphics.Color
import com.fredy.askquestions.features.data.enums.DisplayMode
import java.time.LocalTime

sealed interface PreferencesEvent {
    data class SelectDisplayMode(val displayMode: DisplayMode) : PreferencesEvent
    object ToggleBioAuth : PreferencesEvent
    object ToggleAutoLogin : PreferencesEvent
    object ShowColorPallet : PreferencesEvent
    object HideColorPallet : PreferencesEvent

}