/**
 * 交易仓库类
 * 
 * 处理交易数据的业务逻辑，包括交易的增删改查和统计计算
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.repository

import com.ledger.app.data.dao.CategoryAmount
import com.ledger.app.data.dao.DailyTotal
import com.ledger.app.data.dao.TransactionDao
import com.ledger.app.data.entity.TransactionEntity
import com.ledger.app.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import com.ledger.app.utils.common.LogUtils

/**
 * 交易仓库
 * 
 * 封装交易相关的数据库操作和业务逻辑
 */
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val transactionDao: TransactionDao) {
    private val TAG = "TransactionRepository"
    
    /**
     * 插入交易
     * 
     * @param transaction 交易实体
     * @return 插入的交易ID
     */
    suspend fun insert(transaction: TransactionEntity): Long {
        LogUtils.d(TAG, "Inserting transaction: $transaction")
        val id = transactionDao.insert(transaction)
        LogUtils.d(TAG, "Inserted transaction with id: $id")
        return id
    }

    /**
     * 更新交易
     * 
     * @param transaction 交易实体
     */
    suspend fun update(transaction: TransactionEntity) {
        LogUtils.d(TAG, "Updating transaction: $transaction")
        transactionDao.update(transaction)
        LogUtils.d(TAG, "Updated transaction successfully")
    }

    /**
     * 删除交易
     * 
     * @param transaction 交易实体
     */
    suspend fun delete(transaction: TransactionEntity) {
        LogUtils.d(TAG, "Deleting transaction: $transaction")
        transactionDao.delete(transaction)
        LogUtils.d(TAG, "Deleted transaction successfully")
    }

    /**
     * 根据ID获取交易
     * 
     * @param id 交易ID
     * @return 交易实体，不存在则返回null
     */
    suspend fun getById(id: Long): TransactionEntity? {
        LogUtils.d(TAG, "Getting transaction by id: $id")
        val transaction = transactionDao.getById(id)
        LogUtils.d(TAG, "Got transaction: $transaction")
        return transaction
    }

    /**
     * 获取所有交易
     * 
     * @return 交易列表流
     */
    fun getAll(): Flow<List<TransactionEntity>> {
        LogUtils.d(TAG, "Getting all transactions")
        return transactionDao.getAll()
    }

    /**
     * 根据日期范围获取交易
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 交易列表流
     */
    fun getByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TransactionEntity>> {
        LogUtils.d(TAG, "Getting transactions by date range: $startDate to $endDate")
        return transactionDao.getByDateRange(startDate, endDate)
    }

    /**
     * 根据分类获取交易
     * 
     * @param categoryId 分类ID
     * @return 交易列表流
     */
    fun getByCategory(categoryId: Long): Flow<List<TransactionEntity>> {
        LogUtils.d(TAG, "Getting transactions by category: $categoryId")
        return transactionDao.getByCategory(categoryId)
    }

    /**
     * 根据类型获取交易
     * 
     * @param type 交易类型
     * @return 交易列表流
     */
    fun getByType(type: TransactionType): Flow<List<TransactionEntity>> {
        LogUtils.d(TAG, "Getting transactions by type: $type")
        return transactionDao.getByType(type)
    }

    /**
     * 搜索交易
     * 
     * @param query 搜索关键词
     * @return 交易列表流
     */
    fun search(query: String): Flow<List<TransactionEntity>> {
        LogUtils.d(TAG, "Searching transactions with query: $query")
        return transactionDao.search(query)
    }

    /**
     * 获取收入总额
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 收入总额
     */
    suspend fun getTotalIncome(startDate: LocalDateTime, endDate: LocalDateTime): Double {
        LogUtils.d(TAG, "Getting total income from $startDate to $endDate")
        val total = transactionDao.getTotalByTypeAndDateRange(TransactionType.INCOME, startDate, endDate) ?: 0.0
        LogUtils.d(TAG, "Total income: $total")
        return total
    }

    /**
     * 获取支出总额
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 支出总额
     */
    suspend fun getTotalExpense(startDate: LocalDateTime, endDate: LocalDateTime): Double {
        LogUtils.d(TAG, "Getting total expense from $startDate to $endDate")
        val total = transactionDao.getTotalByTypeAndDateRange(TransactionType.EXPENSE, startDate, endDate) ?: 0.0
        LogUtils.d(TAG, "Total expense: $total")
        return total
    }

    /**
     * 获取余额
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 余额（收入-支出）
     */
    suspend fun getBalance(startDate: LocalDateTime, endDate: LocalDateTime): Double {
        LogUtils.d(TAG, "Getting balance from $startDate to $endDate")
        val income = getTotalIncome(startDate, endDate)
        val expense = getTotalExpense(startDate, endDate)
        val balance = income - expense
        LogUtils.d(TAG, "Balance: $balance (Income: $income, Expense: $expense)")
        return balance
    }

    /**
     * 获取分类金额 breakdown
     * 
     * @param type 交易类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分类金额列表
     */
    suspend fun getCategoryBreakdown(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<CategoryAmount> {
        LogUtils.d(TAG, "Getting category breakdown for $type from $startDate to $endDate")
        val breakdown = transactionDao.getCategoryBreakdown(type, startDate, endDate)
        LogUtils.d(TAG, "Category breakdown: $breakdown")
        return breakdown
    }

    /**
     * 获取每日金额
     * 
     * @param type 交易类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日金额列表
     */
    suspend fun getDailyTotals(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<DailyTotal> {
        LogUtils.d(TAG, "Getting daily totals for $type from $startDate to $endDate")
        val totals = transactionDao.getDailyTotals(type, startDate, endDate)
        LogUtils.d(TAG, "Daily totals: $totals")
        return totals
    }
}
