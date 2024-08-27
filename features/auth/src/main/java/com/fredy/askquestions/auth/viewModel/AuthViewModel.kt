package com.fredy.askquestions.auth.viewModel

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fredy.askquestions.auth.data.AuthMethod
import com.fredy.askquestions.auth.domain.AuthUseCases
import com.fredy.core.domain.models.User
import com.fredy.core.domain.usecases.UserUseCases
import com.fredy.core.util.resource.DataError
import com.fredy.core.util.resource.Resource
import com.fredy.core.util.resource.ResourceError
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle,
    private val authUseCases: AuthUseCases,
    private val userUseCases: UserUseCases,
    private val currentUserData: User?,
): ViewModel() {
    private val bioAuthStatus = MutableStateFlow(
        false
    )

    private val storedVerificationId = MutableStateFlow(
        ""
    )

    private val _state = MutableStateFlow(
        AuthState()
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(signedInUser = currentUserData)
            }
            onEvent(AuthEvent.GetCurrentUser)
        }
        viewModelScope.launch {
            savedStateHandle.get<Boolean>("bioAuthStatus")?.let { stat ->
                bioAuthStatus.update { stat }
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.GoogleAuth -> {
                viewModelScope.launch {
                    authUseCases.googleSignIn(
                        event.credential
                    ).collect { result ->
                        when (result) {
                            is Resource.Success -> {
                                userUseCases.insertUser(
                                    result.data.user
                                )
                                _state.update {
                                    it.copy(
                                        authResource = Resource.Success(
                                            "Google Auth Success"
                                        ),
                                        isSignedIn = true,
                                        authType = AuthMethod.Google
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _state.update {
                                    it.copy(
                                        authResource = Resource.Loading(),
                                        authType = AuthMethod.Google
                                    )
                                }
                            }

                            is Resource.Error -> {

                                _state.update {
                                    it.copy(
                                        authResource = Resource.Error(result.error),
                                        authType = AuthMethod.Google
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AuthEvent.SendOtp -> {
                viewModelScope.launch {
                    authUseCases.sendOtp(
                        event.context,
                        event.phoneNumber
                    ).collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                _state.update {
                                    it.copy(
                                        sendOtpResource = result,
                                    )
                                }
                            }

                            is Resource.Loading -> {
                                _state.update {
                                    it.copy(
                                        sendOtpResource = result,
                                        authType = AuthMethod.SendOTP
                                    )
                                }
                            }

                            is Resource.Success -> {
                                event.onCodeSent()
                                storedVerificationId.update { result.data!! }
                                _state.update {
                                    it.copy(
                                        sendOtpResource = result,
                                        authType = AuthMethod.None
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AuthEvent.VerifyPhoneNumber -> {
                viewModelScope.launch {
                    authUseCases.verifyPhoneNumber(
//                        event.context,
                        storedVerificationId.value,
                        event.code
                    ).collect { result ->
                        if (result is Resource.Success) {
                            userUseCases.insertUser(
                                result.data.user
                            )
                            _state.update {
                                it.copy(isSignedIn = true)
                            }
                        }
                        _state.update {
                            it.copy(
                                authResource = Resource.Success(
                                    "Phone Number Auth Success"
                                ),
                                authType = AuthMethod.PhoneOTP
                            )
                        }
                    }
                }
            }

            is AuthEvent.UpdateUserData -> {
                viewModelScope.launch {
                    _state.value.signedInUser!!.run {
                        val profilePictureUrl = if (event.photoUrl != Uri.EMPTY) {
                            userUseCases.uploadProfilePicture(
                                uid,
                                event.photoUrl
                            )
                        } else {
                            profilePictureUrl
                        }
                        authUseCases.updateUserInformation(
                            profilePictureUrl?.toUri(),
                            event.username,
                            event.oldPassword,
                            event.password
                        ).collect { updateResource ->
                            if (updateResource is Resource.Success) {
                                val user = User(
                                    uid = uid,
                                    username = event.username,
                                    email = email,
                                    phone = phone,
                                    profilePictureUrl = profilePictureUrl
                                )
                                userUseCases.updateUser(
                                    user
                                )
                            }

                            _state.update {
                                it.copy(
                                    updateResource = updateResource
                                )
                            }
                        }
                    }
                    onEvent(AuthEvent.GetCurrentUser)
                }
            }

            AuthEvent.GetCurrentUser -> {
                viewModelScope.launch {
                    userUseCases.getCurrentUser().collectLatest { currentUser ->
                        if (currentUser is Resource.Success) {
                            currentUser.data?.let { user ->
                                _state.update {
                                    it.copy(
                                        signedInUser = user,
                                        isSignedIn = true,
                                        updateResource = Resource.Success(
                                            ""
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AuthEvent.SignOut -> {
                _state.update {
                    AuthState()
                }
                viewModelScope.launch {
                    authUseCases.signOut()
                }
            }

            AuthEvent.BioAuth -> {
                viewModelScope.launch {
                    if (bioAuthStatus.value && currentUserData != null && state.value.isSignedIn) {
                        authUseCases.bioAuth(
                            context
                        ).collectLatest { authResource ->
                            _state.update {
                                it.copy(
                                    authResource = authResource
                                )
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                authResource = Resource.Error(
                                    DataError.Authentication.INVALID_CREDENTIALS
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}



