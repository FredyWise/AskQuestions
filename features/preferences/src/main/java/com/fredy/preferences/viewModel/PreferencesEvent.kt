package com.fredy.preferences.viewModel

import com.fredy.preferences.data.DisplayMode


sealed interface PreferencesEvent {
    data class SelectDisplayMode(val displayMode: DisplayMode) : PreferencesEvent
    object ToggleBioAuth : PreferencesEvent
    object ToggleAutoLogin : PreferencesEvent
    object ShowColorPallet : PreferencesEvent
    object HideColorPallet : PreferencesEvent


}