package com.fredy.askquestions.features.ui.viewmodels.PreferencesViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fredy.askquestions.features.domain.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesRepository: PreferencesRepository,
): ViewModel() {
    init {
        viewModelScope.launch {
            preferencesRepository.getAllPreferenceSettings().collect { savedState ->
                _state.update {
                    savedState
                }
            }
        }
    }

    private val _state = MutableStateFlow(
        PreferencesState()
    )

    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        PreferencesState()
    )

    fun onEvent(event: PreferencesEvent) {
        viewModelScope.launch {
            when (event) {
                is PreferencesEvent.SelectDisplayMode -> {
                    preferencesRepository.saveDisplayMode(
                        event.displayMode
                    )
                    _state.update {
                        it.copy(displayMode = event.displayMode)
                    }
                }

                PreferencesEvent.ToggleAutoLogin -> {
                    preferencesRepository.saveAutoLogin(
                        !_state.value.autoLogin
                    )
                    _state.update {
                        it.copy(autoLogin = !it.autoLogin)
                    }
                }

                PreferencesEvent.ToggleBioAuth -> {
                    preferencesRepository.saveBioAuth(
                        !_state.value.bioAuth
                    )
                    if (!_state.value.bioAuth) {
                        preferencesRepository.saveAutoLogin(
                            true
                        )
                        _state.update {
                            it.copy(autoLogin = true)
                        }
                    }
                    _state.update {
                        it.copy(bioAuth = !it.bioAuth)
                    }
                }

                PreferencesEvent.HideColorPallet -> {
                    _state.update {
                        it.copy(isShowColorPallet = false)
                    }
                }

                PreferencesEvent.ShowColorPallet -> {
                    _state.update {
                        it.copy(isShowColorPallet = true)
                    }
                }
            }
        }
    }


    private fun calculateDelayUntilTime(time: LocalTime): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        calendar.set(
            Calendar.HOUR_OF_DAY,
            time.hour
        )
        calendar.set(Calendar.MINUTE, time.minute)
        calendar.set(Calendar.SECOND, time.second)
        calendar.set(Calendar.MILLISECOND, 0)
        val nextTime = calendar.timeInMillis
        if (nextTime < now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return nextTime - now
    }

}

