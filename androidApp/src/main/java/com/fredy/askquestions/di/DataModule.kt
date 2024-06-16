package com.fredy.askquestions.di

import com.fredy.askquestions.features.data.database.firebase.ChatDataSource
import com.fredy.askquestions.features.data.database.firebase.ChatDataSourceImpl
import com.fredy.askquestions.features.data.database.firebase.UserDataSource
import com.fredy.askquestions.features.data.database.firebase.UserDataSourceImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

//    @Provides
//    @Singleton
//    fun savingsDatabase(@ApplicationContext appContext: Context): SavingsDatabase {
//        return Room.databaseBuilder(
//            appContext,
//            SavingsDatabase::class.java,
//            "savings_database"
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideRecordDao(savingsDatabase: SavingsDatabase): RecordDao = savingsDatabase.recordDao
//
//    @Provides
//    @Singleton
//    fun provideAccountDao(savingsDatabase: SavingsDatabase): WalletDao = savingsDatabase.walletDao
//
//    @Provides
//    @Singleton
//    fun provideCategoryDao(savingsDatabase: SavingsDatabase): CategoryDao =
//        savingsDatabase.categoryDao
//
//    @Provides
//    @Singleton
//    fun provideBookDao(savingsDatabase: SavingsDatabase): BookDao =
//        savingsDatabase.bookDao
//
//    @Provides
//    @Singleton
//    fun provideUserDao(savingsDatabase: SavingsDatabase): UserDao = savingsDatabase.userDao
//
//    @Provides
//    @Singleton
//    fun provideCurrencyCacheDao(savingsDatabase: SavingsDatabase): CurrencyCacheDao =
//        savingsDatabase.currencyCache
//
//    @Provides
//    @Singleton
//    fun provideCurrencyDao(savingsDatabase: SavingsDatabase): CurrencyDao =
//        savingsDatabase.currency

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


}
