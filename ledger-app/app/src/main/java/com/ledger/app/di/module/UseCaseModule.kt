/**
 * 用例模块
 * 提供用例的依赖注入
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.di.module

import com.ledger.app.data.repository.CategoryRepository
import com.ledger.app.data.repository.TransactionRepository
import com.ledger.app.domain.usecase.CategoryUseCase
import com.ledger.app.domain.usecase.StatisticsUseCase
import com.ledger.app.domain.usecase.TransactionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 用例模块
 * 提供交易、分类和统计用例的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    /**
     * 提供交易用例实例
     *
     * @param transactionRepository 交易仓库
     * @param categoryRepository 分类仓库
     * @return 交易用例实例
     */
    @Provides
    @Singleton
    fun provideTransactionUseCase(
        transactionRepository: TransactionRepository,
        categoryRepository: CategoryRepository
    ): TransactionUseCase {
        return TransactionUseCase(transactionRepository, categoryRepository)
    }

    /**
     * 提供分类用例实例
     *
     * @param categoryRepository 分类仓库
     * @param transactionRepository 交易仓库
     * @return 分类用例实例
     */
    @Provides
    @Singleton
    fun provideCategoryUseCase(
        categoryRepository: CategoryRepository,
        transactionRepository: TransactionRepository
    ): CategoryUseCase {
        return CategoryUseCase(categoryRepository, transactionRepository)
    }

    /**
     * 提供统计用例实例
     *
     * @param transactionRepository 交易仓库
     * @param categoryRepository 分类仓库
     * @return 统计用例实例
     */
    @Provides
    @Singleton
    fun provideStatisticsUseCase(
        transactionRepository: TransactionRepository,
        categoryRepository: CategoryRepository
    ): StatisticsUseCase {
        return StatisticsUseCase(transactionRepository, categoryRepository)
    }
}
