package com.fredy.askquestions.auth.viewModel


import com.fredy.core.resource.DataError
import com.fredy.core.resource.Resource
import com.google.firebase.auth.AuthResult

data class AuthState(
    val updateResource: Resource<String, DataError.Authentication> = Resource.Success(
        ""
    ),
    val bioAuthResource: Resource<String,DataError.Authentication> = Resource.Loading(),
    val authResource: Resource<AuthResult,DataError.Authentication> = Resource.Loading(),
    val sendOtpResource: Resource<String,DataError.Authentication> = Resource.Loading(),
    val authType: AuthMethod = AuthMethod.None,
    val signedInUser: User? = null,
    val isSignedIn: Boolean = false,
)