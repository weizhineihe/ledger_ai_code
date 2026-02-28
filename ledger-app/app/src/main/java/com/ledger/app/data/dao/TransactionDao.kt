/**
 * 交易数据访问对象
 * 
 * 用于交易实体的数据库操作
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ledger.app.data.entity.TransactionEntity
import com.ledger.app.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 交易DAO接口
 * 
 * 定义交易实体的数据库操作方法
 */
@Dao
interface TransactionDao {
    /**
     * 插入交易
     * 
     * @param transaction 交易实体
     * @return 插入的交易ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    /**
     * 更新交易
     * 
     * @param transaction 交易实体
     */
    @Update
    suspend fun update(transaction: TransactionEntity)

    /**
     * 删除交易
     * 
     * @param transaction 交易实体
     */
    @Delete
    suspend fun delete(transaction: TransactionEntity)

    /**
     * 根据ID获取交易
     * 
     * @param id 交易ID
     * @return 交易实体，不存在则返回null
     */
    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getById(id: Long): TransactionEntity?

    /**
     * 获取所有交易
     * 
     * @return 交易列表流，按日期降序排序
     */
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<TransactionEntity>>

    /**
     * 根据日期范围获取交易
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 交易列表流，按日期降序排序
     */
    @Query("""
        SELECT * FROM transactions 
        WHERE date BETWEEN :startDate AND :endDate 
        ORDER BY date DESC
    """)
    fun getByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TransactionEntity>>

    /**
     * 根据分类获取交易
     * 
     * @param categoryId 分类ID
     * @return 交易列表流，按日期降序排序
     */
    @Query("""
        SELECT * FROM transactions 
        WHERE categoryId = :categoryId 
        ORDER BY date DESC
    """)
    fun getByCategory(categoryId: Long): Flow<List<TransactionEntity>>

    /**
     * 根据类型获取交易
     * 
     * @param type 交易类型
     * @return 交易列表流，按日期降序排序
     */
    @Query("""
        SELECT * FROM transactions 
        WHERE type = :type 
        ORDER BY date DESC
    """)
    fun getByType(type: TransactionType): Flow<List<TransactionEntity>>

    /**
     * 搜索交易
     * 
     * @param query 搜索关键词
     * @return 交易列表流，按日期降序排序
     */
    @Query("""
        SELECT * FROM transactions 
        WHERE description LIKE '%' || :query || '%' 
        ORDER BY date DESC
    """)
    fun search(query: String): Flow<List<TransactionEntity>>

    /**
     * 根据类型和日期范围获取总金额
     * 
     * @param type 交易类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 总金额，无数据则返回null
     */
    @Query("""
        SELECT SUM(amount) FROM transactions 
        WHERE type = :type 
        AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalByTypeAndDateRange(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): Double?

    /**
     * 获取分类支出/收入 breakdown
     * 
     * @param type 交易类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分类金额列表
     */
    @Query("""
        SELECT categoryId, SUM(amount) as amount FROM transactions 
        WHERE type = :type 
        AND date BETWEEN :startDate AND :endDate 
        GROUP BY categoryId
    """)
    suspend fun getCategoryBreakdown(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<CategoryAmount>

    /**
     * 获取每日总金额
     * 
     * @param type 交易类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日金额列表
     */
    @Query("""
        SELECT DATE(date) as date, SUM(amount) as amount 
        FROM transactions 
        WHERE type = :type 
        AND date BETWEEN :startDate AND :endDate 
        GROUP BY DATE(date) 
        ORDER BY date
    """)
    suspend fun getDailyTotals(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<DailyTotal>
}

/**
 * 分类金额数据类
 * 
 * @param categoryId 分类ID
 * @param amount 金额
 */
data class CategoryAmount(
    val categoryId: Long,
    val amount: Double
)

/**
 * 每日金额数据类
 * 
 * @param date 日期
 * @param amount 金额
 */
data class DailyTotal(
    val date: String,
    val amount: Double
)
