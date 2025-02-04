package com.fredy.askquestions.di

import android.content.Context
import androidx.room.Room
import com.fredy.askquestions.features.data.database.firebase.dao.ChatDataSource
import com.fredy.askquestions.features.data.database.firebase.dao.ChatDataSourceImpl
import com.fredy.askquestions.features.data.database.firebase.dao.MessageDataSource
import com.fredy.askquestions.features.data.database.firebase.dao.MessageDataSourceImpl
import com.fredy.askquestions.features.data.database.firebase.dao.UserDataSource
import com.fredy.askquestions.features.data.database.firebase.dao.UserDataSourceImpl
import com.fredy.askquestions.features.data.database.room.ChattingDatabase
import com.fredy.askquestions.features.data.database.room.dao.ChatDao
import com.fredy.askquestions.features.data.database.room.dao.MessageDao
import com.fredy.askquestions.features.data.database.room.dao.UserDao
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun savingsDatabase(@ApplicationContext appContext: Context): ChattingDatabase {
        return Room.databaseBuilder(
            appContext,
            ChattingDatabase::class.java,
            "chatting_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(savingsDatabase: ChattingDatabase): UserDao = savingsDatabase.userDao

    @Provides
    @Singleton
    fun provideChatDao(savingsDatabase: ChattingDatabase): ChatDao = savingsDatabase.chatDao

    @Provides
    @Singleton
    fun provideMessageDao(savingsDatabase: ChattingDatabase): MessageDao = savingsDatabase.messageDao

    @Provides
    @Singleton
    fun provideUserDataSource(firestore: FirebaseFirestore): UserDataSource = UserDataSourceImpl(
        firestore
    )

    @Provides
    @Singleton
    fun provideChatDataSource(firestore: FirebaseFirestore): ChatDataSource = ChatDataSourceImpl(
        firestore
    )

    @Provides
    @Singleton
    fun provideMessageDataSource(firestore: FirebaseFirestore): MessageDataSource = MessageDataSourceImpl(
        firestore
    )


}
