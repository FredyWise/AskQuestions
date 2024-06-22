package com.fredy.askquestions.features.domain.usecases.AuthUseCases

import android.content.Context
import com.fredy.askquestions.MainActivity
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SendOtp(
    private val firebaseAuth: FirebaseAuth
) {
    operator fun invoke(
        context: Context,
        phoneNumber: String,
    ): Flow<Resource<String, DataError.Authentication>> = callbackFlow {
        trySend(Resource.Loading())
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(
                credential: PhoneAuthCredential
            ) {
                Timber.d(
                    "verification completed"
                )
                firebaseAuth.signInWithCredential(
                    credential
                )
            }

            override fun onVerificationFailed(
                p0: FirebaseException
            ) {
                Timber.e("verification failed: " + p0)
                p0.message?.let {
                    trySend(Resource.Error(DataError.Authentication.INVALID_CREDENTIALS))
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Timber.d("onCodeSent: " + verificationId)
                trySend(
                    Resource.Success(
                        verificationId
                    )
                )
            }
        }

        val options = PhoneAuthOptions.newBuilder(
            firebaseAuth
        ).setPhoneNumber(
            phoneNumber
        ).setTimeout(
            60L, TimeUnit.SECONDS
        ).setActivity(context as MainActivity).setCallbacks(
            callback
        ).build()

        Timber.d(options.toString())
        PhoneAuthProvider.verifyPhoneNumber(
            options
        )

        awaitClose {}
    }
}