package com.fredy.mysavings.DI

import com.fredy.askquestions.features.data.database.room.ChattingDatabase
import com.fredy.askquestions.features.domain.repositories.ChatRepository
import com.fredy.askquestions.features.domain.repositories.UserRepository
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.AuthUseCases
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.GoogleSignIn
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.SendOtp
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.SignOut
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.UpdateUserInformation
import com.fredy.askquestions.features.domain.usecases.AuthUseCases.VerifyPhoneNumber
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.ChatUseCases
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.DeleteChat
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.DeleteMessage
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.GetAllChatsOrderedByName
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.GetAllMessagesInChat
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.GetChat
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.SearchChats
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.UpsertChat
import com.fredy.askquestions.features.domain.usecases.ChatUseCases.UpsertMessage
import com.fredy.askquestions.features.domain.usecases.UserUseCases.DeleteUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.GetCurrentUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.GetUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.InsertUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.SearchUsers
import com.fredy.askquestions.features.domain.usecases.UserUseCases.UpdateUser
import com.fredy.askquestions.features.domain.usecases.UserUseCases.UploadProfilePicture
import com.fredy.askquestions.features.domain.usecases.UserUseCases.UserUseCases
import com.fredy.mysavings.Feature.Domain.UseCases.UserUseCases.GetAllUsersOrderedByName
import com.google.firebase.auth.FirebaseAuth
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
    fun provideAuthUseCases(
        firebaseAuth: FirebaseAuth,
//        oneTapClient: SignInClient,
    ): AuthUseCases = AuthUseCases(
        updateUserInformation = UpdateUserInformation(
            firebaseAuth
        ),
        googleSignIn = GoogleSignIn(firebaseAuth),
        sendOtp = SendOtp(firebaseAuth),
        verifyPhoneNumber = VerifyPhoneNumber(
            firebaseAuth
        ),
        signOut = SignOut(firebaseAuth), //, oneTapClient
    )

    @Provides
    @Singleton
    fun provideUserUseCases(
        userRepository: UserRepository,
        firebaseMessaging: FirebaseMessaging
    ): UserUseCases = UserUseCases(
        insertUser = InsertUser(userRepository,firebaseMessaging),
        updateUser = UpdateUser(userRepository,firebaseMessaging),
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

    @Provides
    @Singleton
    fun provideChatUseCases(
        chattingDatabase: ChattingDatabase,
        chatRepository: ChatRepository,
        userRepository: UserRepository
    ): ChatUseCases = ChatUseCases(
        upsertChat = UpsertChat(chatRepository),
        upsertMessage = UpsertMessage(
            chatRepository
        ),
        deleteChat = DeleteChat(chatRepository),
        getChat = GetChat(chatRepository),
        getAllChatsOrderedByName = GetAllChatsOrderedByName(
            chatRepository
        ),
        searchChats = SearchChats(chatRepository),
        getAllMessagesInChat = GetAllMessagesInChat(
            chattingDatabase,chatRepository,userRepository
        ),
        deleteMessage = DeleteMessage(
            chatRepository
        ),
    )

}