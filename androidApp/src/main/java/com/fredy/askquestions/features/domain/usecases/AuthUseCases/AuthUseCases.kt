package com.fredy.askquestions.features.domain.usecases.AuthUseCases


data class AuthUseCases(
    val loginUser: LoginUser,
    val registerUser: RegisterUser,
    val updateUserInformation: UpdateUserInformation,
    val googleSignIn: GoogleSignIn,
    val sendOtp: SendOtp,
    val verifyPhoneNumber: VerifyPhoneNumber,
    val signOut: SignOut,
)

