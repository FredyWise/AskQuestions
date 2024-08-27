package com.fredy.core.di

import com.fredy.core.data.database.firebase.dao.UserDataSource
import com.fredy.core.data.repositoryImpl.UserRepositoryImpl
import com.fredy.core.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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


}