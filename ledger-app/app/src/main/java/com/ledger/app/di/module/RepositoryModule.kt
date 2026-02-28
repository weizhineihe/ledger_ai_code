/**
 * 仓库模块
 * 提供仓库的依赖注入
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.di.module

import com.ledger.app.data.dao.CategoryDao
import com.ledger.app.data.dao.TransactionDao
import com.ledger.app.data.repository.CategoryRepository
import com.ledger.app.data.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 仓库模块
 * 提供交易和分类仓库的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * 提供交易仓库实例
     *
     * @param transactionDao 交易DAO
     * @return 交易仓库实例
     */
    @Provides
    @Singleton
    fun provideTransactionRepository(transactionDao: TransactionDao): TransactionRepository {
        return TransactionRepository(transactionDao)
    }

    /**
     * 提供分类仓库实例
     *
     * @param categoryDao 分类DAO
     * @return 分类仓库实例
     */
    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }
}
