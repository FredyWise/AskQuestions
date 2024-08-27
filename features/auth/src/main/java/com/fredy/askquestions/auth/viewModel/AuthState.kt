package com.fredy.askquestions.auth.viewModel


import com.fredy.askquestions.auth.data.AuthMethod
import com.fredy.core.domain.models.User
import com.fredy.core.util.resource.DataError
import com.fredy.core.util.resource.Resource

data class AuthState(
    val updateResource: Resource<String, DataError.Authentication> = Resource.Success(
        ""
    ),
    val authResource: Resource<String, DataError.Authentication> = Resource.Loading(),
    val sendOtpResource: Resource<String, DataError.Authentication> = Resource.Loading(),
    val authType: AuthMethod = AuthMethod.None,
    val signedInUser: User? = null,
    val isSignedIn: Boolean = false,
)