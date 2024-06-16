package com.fredy.askquestions.di

import android.content.Context
import com.fredy.askquestions.features.data.apis.ApiConfiguration
import com.fredy.askquestions.features.domain.models.User
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(modelName = ApiConfiguration.GeminiModel.FLASH_MODEL_1_5, apiKey = ApiConfiguration.GeminiModel.API_KEY)
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

//    @Provides
//    @Singleton
//    fun providesSignInClient(@ApplicationContext appContext: Context): SignInClient =
//        Identity.getSignInClient(
//            appContext
//        )

    @Provides
    fun providesCurrentUserData(firebaseAuth: FirebaseAuth): User? = firebaseAuth.currentUser?.run {
        User(
            uid = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl.toString()
        )
    }

}
