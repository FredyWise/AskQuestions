package com.fredy.askquestions.auth.domain


data class AuthUseCases(
    val updateUserInformation: UpdateUserInformation,
    val googleSignIn: GoogleSignIn,
    val sendOtp: SendOtp,
    val verifyPhoneNumber: VerifyPhoneNumber,
    val signOut: SignOut,
    val bioAuth: BioAuth,
)

