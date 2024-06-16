package com.fredy.askquestions.features.ui.viewmodels.AuthViewModel

import com.fredy.askquestions.features.data.enums.AuthMethod
import com.fredy.askquestions.features.domain.models.User
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.google.firebase.auth.AuthResult

data class AuthState(
    val updateResource: Resource<String,DataError.Authentication> = Resource.Success(
        ""
    ),
    val bioAuthResource: Resource<String,DataError.Authentication> = Resource.Loading(),
    val authResource: Resource<AuthResult,DataError.Authentication> = Resource.Loading(),
    val sendOtpResource: Resource<String,DataError.Authentication> = Resource.Loading(),
    val authType: AuthMethod = AuthMethod.None,
    val signedInUser: User? = null,
    val isSignedIn: Boolean = false,
)