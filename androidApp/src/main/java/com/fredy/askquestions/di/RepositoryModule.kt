package com.fredy.mysavings.DI

import android.content.Context
import com.fredy.askquestions.features.data.database.firebase.UserDataSource
import com.fredy.askquestions.features.data.repositoryImpl.PreferencesRepositoryImpl
import com.fredy.askquestions.features.data.repositoryImpl.UserRepositoryImpl
import com.fredy.askquestions.features.domain.repositories.PreferencesRepository
import com.fredy.askquestions.features.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        userDataSource: UserDataSource,
    ): UserRepository = UserRepositoryImpl(
        firebaseAuth, userDataSource
    )

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext appContext: Context
    ): PreferencesRepository = PreferencesRepositoryImpl(
        appContext
    )

}