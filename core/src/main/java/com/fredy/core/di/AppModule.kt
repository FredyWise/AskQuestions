package com.fredy.core.di

import com.fredy.askquestions.features.data.apis.FCM.FcmApi
import com.fredy.core.data.mappers.toUser
import com.fredy.core.domain.models.User
import com.fredy.core.data.util.Configuration
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.internal.Provider
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFcmInstance() = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideFcmApi(): FcmApi = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create()

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(modelName = Configuration.GeminiModel.FLASH_MODEL_1_5, apiKey = Configuration.GeminiModel.API_KEY)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        val firestore = FirebaseFirestore.getInstance()
        return firestore
    }

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesCurrentUser(firebaseAuth: FirebaseAuth): FirebaseUser? = firebaseAuth.currentUser

    @Provides
    fun providesFirebaseUserProvider(firebaseUser: FirebaseUser?): Provider<FirebaseUser> {
        return Provider { firebaseUser ?: throw IllegalStateException("User not authenticated") }
    }
    @Provides
    fun providesCurrentUserData(firebaseUser: FirebaseUser?): User? = firebaseUser?.toUser()

}
