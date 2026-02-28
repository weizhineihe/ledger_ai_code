/**
 * 应用模块
 * 提供应用级别的依赖注入
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.di.module

import android.content.Context
import androidx.room.Room
import com.ledger.app.data.dao.CategoryDao
import com.ledger.app.data.dao.TransactionDao
import com.ledger.app.data.database.LedgerDatabase
import com.ledger.app.utils.common.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 应用模块
 * 提供数据库和DAO的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * 提供账本数据库实例
     *
     * @param context 应用上下文
     * @return 账本数据库实例
     */
    @Provides
    @Singleton
    fun provideLedgerDatabase(@ApplicationContext context: Context): LedgerDatabase {
        return Room.databaseBuilder(
            context,
            LedgerDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .build()
    }

    /**
     * 提供交易DAO实例
     *
     * @param database 账本数据库
     * @return 交易DAO实例
     */
    @Provides
    @Singleton
    fun provideTransactionDao(database: LedgerDatabase) : TransactionDao {
        return database.transactionDao()
    }

    /**
     * 提供分类DAO实例
     *
     * @param database 账本数据库
     * @return 分类DAO实例
     */
    @Provides
    @Singleton
    fun provideCategoryDao(database: LedgerDatabase): CategoryDao {
        return database.categoryDao()
    }
}
