package com.fredy.askquestions.auth.di

import com.fredy.askquestions.auth.domain.AuthUseCases
import com.fredy.askquestions.auth.domain.BioAuth
import com.fredy.askquestions.auth.domain.GoogleSignIn
import com.fredy.askquestions.auth.domain.SendOtp
import com.fredy.askquestions.auth.domain.SignOut
import com.fredy.askquestions.auth.domain.UpdateUserInformation
import com.fredy.askquestions.auth.domain.VerifyPhoneNumber
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthUseCases(
        firebaseAuth: FirebaseAuth,
    ): AuthUseCases = AuthUseCases(
        updateUserInformation = UpdateUserInformation(
            firebaseAuth
        ),
        googleSignIn = GoogleSignIn(firebaseAuth),
        sendOtp = SendOtp(firebaseAuth),
        verifyPhoneNumber = VerifyPhoneNumber(
            firebaseAuth
        ),
        signOut = SignOut(firebaseAuth),
        bioAuth = BioAuth()
    )
}