package com.fredy.mysavings.DI

import com.fredy.askquestions.features.domain.usecases.AuthUseCases.AuthUseCases
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.GoogleSignIn
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.SendOtp
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.SignOut
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.UpdateUserInformation
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.VerifyPhoneNumber
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.ChatUseCases
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.GetChat
import com.fredy.askquestions.features.domain.usecases.UserUseCases.DeleteUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.GetCurrentUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.GetUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.InsertUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.SearchUsers
import com.fredy.askquestions.features.domain.usecases.UserUseCases.UpdateUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.UserUseCases
import com.fredy.askquestions.features.domain.repositories.UserRepository
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.DeleteChat
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.GetAllChatsOrderedByName
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.InsertChat
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.SearchChats
import com.fredy.mysavings.Feature.Domain.UseCases.UserUseCases.GetAllUsersOrderedByName
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
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
    fun provideAuthUseCases(
        firebaseAuth: FirebaseAuth,
        oneTapClient: SignInClient,
    ): AuthUseCases = AuthUseCases(
        updateUserInformation = UpdateUserInformation(firebaseAuth),
        googleSignIn = GoogleSignIn(firebaseAuth),
        sendOtp = SendOtp(firebaseAuth),
        verifyPhoneNumber = VerifyPhoneNumber(firebaseAuth),
        signOut = SignOut(firebaseAuth), //, oneTapClient
    )

    @Provides
    @Singleton
    fun provideUserUseCases(
        userRepository: UserRepository
    ): UserUseCases = UserUseCases(
        insertUser = InsertUser(userRepository),
        updateUser = UpdateUser(userRepository),
        deleteUser = DeleteUser(userRepository),
        getUser = GetUser(userRepository),
        getCurrentUser = GetCurrentUser(userRepository),
        getAllUsersOrderedByName = GetAllUsersOrderedByName(userRepository),
        searchUsers = SearchUsers(userRepository)
    )

    @Provides
    @Singleton
    fun provideChatUseCases(
        chatRepository: ChatRepository
    ): ChatUseCases = ChatUseCases(
        insertChat = InsertChat(chatRepository),
        deleteChat = DeleteChat(chatRepository),
        getChat = GetChat(chatRepository),
        getAllChatsOrderedByName = GetAllChatsOrderedByName(chatRepository),
        searchChats = SearchChats(chatRepository)
    )

}