package com.fredy.askquestions.features.ui.viewmodels.AuthViewModel

import android.content.Context
import android.hardware.biometrics.BiometricPrompt
import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback
import android.net.Uri
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fredy.askquestions.auth.viewModel.AuthEvent
import com.fredy.askquestions.auth.viewModel.AuthState
import com.fredy.askquestions.features.data.enums.AuthMethod
import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.AuthUseCases
import com.fredy.askquestions.features.domain.usecases.UserUseCases.UserUseCases
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.fredy.askquestions.features.domain.repositories.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authUseCases: AuthUseCases,
    private val preferencesRepository: PreferencesRepository,
    private val userUseCases: UserUseCases,
    private val currentUserData: User?,
): ViewModel() {

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
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.GoogleAuth -> {
                viewModelScope.launch {
                    authUseCases.googleSignIn(
                        event.credential
                    ).collect { result ->
                        if (result is Resource.Success) {
                            userUseCases.insertUser(result.data.user)
                            _state.update {
                                it.copy(isSignedIn = true)
                            }
                        }
                        _state.update {
                            it.copy(
                                authResource = result,
                                authType = AuthMethod.Google
                            )
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
                            userUseCases.insertUser(result.data.user)
                            _state.update {
                                it.copy(isSignedIn = true)
                            }
                        }
                        _state.update {
                            it.copy(
                                authResource = result,
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
                            when (updateResource) {
                                is Resource.Success -> {
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

                                else -> {
                                }
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
                        when (currentUser) {
                            is Resource.Success -> {
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

                            else -> {
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
                if (preferencesRepository.bioAuthStatus() && currentUserData != null && state.value.isSignedIn) {
                    val title = "Login"
                    val subtitle = "Login into your account"
                    val description = "Put your finger on the fingerprint sensor or scan your face to authorise your account."
                    val negativeText = "Cancel"
                    val executor = ContextCompat.getMainExecutor(
                        context
                    )
                    val biometricPrompt = BiometricPrompt.Builder(
                        context
                    ).apply {
                        setTitle(title)
                        setSubtitle(subtitle)
                        setDescription(
                            description
                        )
                        setConfirmationRequired(
                            false
                        )
                        setNegativeButton(
                            negativeText, executor
                        ) { _, _ ->
                            _state.update {
                                it.copy(
                                    bioAuthResource = Resource.Error(
                                        DataError.Authentication.USER_CANCELLED
                                    )
                                )
                            }
                        }
                    }.build()

                    biometricPrompt.authenticate(
                        CancellationSignal(),
                        executor,
                        object: AuthenticationCallback() {
                            override fun onAuthenticationError(
                                errorCode: Int,
                                errString: CharSequence
                            ) {
                                super.onAuthenticationError(
                                    errorCode,
                                    errString
                                )
                                Timber.e(
                                    "onAuthenticationError:\n error code:$errorCode\n $errString",
                                )
                                _state.update {
                                    it.copy(
                                        bioAuthResource = Resource.Error(
                                            DataError.Authentication.UNKNOWN
                                        )
                                    )
                                }
                            }

                            override fun onAuthenticationSucceeded(
                                result: BiometricPrompt.AuthenticationResult
                            ) {
                                super.onAuthenticationSucceeded(
                                    result
                                )
                                _state.update {
                                    it.copy(
                                        bioAuthResource = Resource.Success(
                                            "Bio Auth Success"
                                        )
                                    )
                                }
                            }

                            override fun onAuthenticationFailed() {
                                super.onAuthenticationFailed()
                                _state.update {
                                    it.copy(
                                        bioAuthResource = Resource.Error(
                                            DataError.Authentication.INVALID_CREDENTIALS
                                        )
                                    )
                                }
                            }
                        },
                    )
                } else {
                    _state.update {
                        it.copy(
                            bioAuthResource = Resource.Error(
                                DataError.Authentication.INVALID_CREDENTIALS
                            )
                        )
                    }
                }
            }

        }
    }



}



