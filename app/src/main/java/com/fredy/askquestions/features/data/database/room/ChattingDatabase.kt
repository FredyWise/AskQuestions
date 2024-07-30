package com.fredy.askquestions.features.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fredy.askquestions.features.data.database.converters.DateTimeConverter
import com.fredy.askquestions.features.data.database.converters.ParticipantsConverter
import com.fredy.askquestions.features.data.database.converters.TimestampConverter
import com.fredy.askquestions.features.data.database.room.dao.ChatDao
import com.fredy.askquestions.features.data.database.room.dao.MessageDao
import com.fredy.askquestions.features.data.database.room.dao.UserDao
import com.fredy.askquestions.features.data.database.room.dto.ChatEntity
import com.fredy.askquestions.features.data.database.room.dto.MessageEntity
import com.fredy.askquestions.features.data.database.room.dto.UserEntity


@TypeConverters(value = [ParticipantsConverter::class, TimestampConverter::class, DateTimeConverter::class])
@Database(
    entities = [ChatEntity::class, MessageEntity::class, UserEntity::class],
    version = 1,
//    exportSchema = true,
//    autoMigrations = [
//        AutoMigration (
//            from = 1,
//            to = 2,
//        )
//    ]
)
abstract class ChattingDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val messageDao: MessageDao
    abstract val userDao: UserDao
}

