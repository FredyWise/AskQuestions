package com.fredy.askquestions.di

import com.fredy.askquestions.features.data.Util.Configuration
import com.fredy.askquestions.features.data.apis.FCM.FcmApi
import com.fredy.askquestions.features.domain.models.User
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
