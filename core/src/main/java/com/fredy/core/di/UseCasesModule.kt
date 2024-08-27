package com.fredy.core.di

import com.fredy.core.domain.repositories.UserRepository
import com.fredy.core.domain.usecases.DeleteUser
import com.fredy.core.domain.usecases.GetAllUsersOrderedByName
import com.fredy.core.domain.usecases.GetCurrentUser
import com.fredy.core.domain.usecases.GetUser
import com.fredy.core.domain.usecases.InsertUser
import com.fredy.core.domain.usecases.SearchUsers
import com.fredy.core.domain.usecases.UpdateUser
import com.fredy.core.domain.usecases.UploadProfilePicture
import com.fredy.core.domain.usecases.UserUseCases
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideUserUseCases(
        userRepository: UserRepository,
        firebaseMessaging: FirebaseMessaging
    ): UserUseCases = UserUseCases(
        insertUser = InsertUser(
            userRepository,
            firebaseMessaging
        ),
        updateUser = UpdateUser(
            userRepository,
            firebaseMessaging
        ),
        deleteUser = DeleteUser(userRepository),
        getUser = GetUser(userRepository),
        getCurrentUser = GetCurrentUser(
            userRepository
        ),
        getAllUsersOrderedByName = GetAllUsersOrderedByName(
            userRepository
        ),
        searchUsers = SearchUsers(userRepository),
        uploadProfilePicture = UploadProfilePicture()
    )

}