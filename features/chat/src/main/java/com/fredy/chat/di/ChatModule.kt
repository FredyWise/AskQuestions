package com.fredy.chat.di

import com.fredy.chat.data.ChatRepositoryImpl
import com.fredy.chat.domain.ChatRepository
import com.fredy.chat.domain.useCases.ChatUseCases
import com.fredy.chat.domain.useCases.DeleteChat
import com.fredy.chat.domain.useCases.DeleteMessage
import com.fredy.chat.domain.useCases.GetAllChatsOrderedByName
import com.fredy.chat.domain.useCases.GetAllMessagesInChat
import com.fredy.chat.domain.useCases.GetChat
import com.fredy.chat.domain.useCases.SearchChats
import com.fredy.chat.domain.useCases.UpsertChat
import com.fredy.chat.domain.useCases.UpsertMessage
import com.fredy.core.data.database.firebase.dao.ChatDataSource
import com.fredy.core.data.database.firebase.dao.MessageDataSource
import com.fredy.core.data.database.firebase.dao.UserDataSource
import com.fredy.core.data.database.room.ChattingDatabase
import com.fredy.core.data.database.room.dao.MessageDao
import com.fredy.core.domain.repositories.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.internal.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatRepository(
        firebaseUserProvider: Provider<FirebaseUser>,
        chatDataSource: ChatDataSource,
        messageDataSource: MessageDataSource,
        userDataSource: UserDataSource,
        messageDao: MessageDao,
    ): ChatRepository = ChatRepositoryImpl(
        firebaseUserProvider,
        chatDataSource,
        messageDataSource,
        userDataSource,
        messageDao
    )


    @Provides
    @Singleton
    fun provideChatUseCases(
        chattingDatabase: ChattingDatabase,
        messageDao: MessageDao,
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
            chattingDatabase,
            messageDao,
            chatRepository,
            userRepository
        ),
        deleteMessage = DeleteMessage(
            chatRepository
        ),
    )
}