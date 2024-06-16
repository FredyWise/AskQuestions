package com.fredy.askquestions.features.domain.usecases.AuthUseCases

import android.content.Context
import com.fredy.askquestions.features.domain.util.Resource.DataError
import com.fredy.askquestions.features.domain.util.Resource.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class VerifyPhoneNumber(
    private val firebaseAuth: FirebaseAuth
) {
    operator fun invoke(
        context: Context,
        verificationId: String,
        code: String
    ): Flow<Resource<AuthResult, DataError.Authentication>> {
        return flow<Resource<AuthResult, DataError.Authentication>> {
            emit(Resource.Loading())
            val credential = PhoneAuthProvider.getCredential(
                verificationId, code
            )
            val result = firebaseAuth.signInWithCredential(
                credential
            ).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(DataError.Authentication.UNKNOWN))
        }
    }
}