/**
 * 统计用例类
 * 处理统计相关的业务逻辑
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.domain.usecase

import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.repository.CategoryRepository
import com.ledger.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

/**
 * 统计用例
 * 提供各种统计数据的计算和获取
 *
 * @param transactionRepository 交易仓库
 * @param categoryRepository 分类仓库
 */
class StatisticsUseCase(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) {
    /**
     * 获取月度汇总
     *
     * @param year 年份
     * @param month 月份
     * @return 月度汇总数据
     */
    suspend fun getMonthlySummary(year: Int, month: Int): MonthlySummary {
        val startDate = LocalDateTime.of(year, month, 1, 0, 0, 0)
        val endDate = startDate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59)

        val totalIncome = transactionRepository.getTotalIncome(startDate, endDate)
        val totalExpense = transactionRepository.getTotalExpense(startDate, endDate)
        val balance = totalIncome - totalExpense

        return MonthlySummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance,
            startDate = startDate,
            endDate = endDate
        )
    }

    /**
     * 获取年度汇总
     *
     * @param year 年份
     * @return 年度汇总数据
     */
    suspend fun getYearlySummary(year: Int): YearlySummary {
        val startDate = LocalDateTime.of(year, 1, 1, 0, 0, 0)
        val endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59)

        val totalIncome = transactionRepository.getTotalIncome(startDate, endDate)
        val totalExpense = transactionRepository.getTotalExpense(startDate, endDate)
        val balance = totalIncome - totalExpense

        return YearlySummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = balance,
            year = year
        )
    }

    /**
     * 获取分类收支明细
     *
     * @param type 交易类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 分类收支明细列表
     */
    suspend fun getCategoryBreakdown(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<CategoryAmountDetail> {
        val categoryBreakdown = transactionRepository.getCategoryBreakdown(type, startDate, endDate)
        
        return categoryBreakdown.mapNotNull { (categoryId, amount) ->
            val category = categoryRepository.getById(categoryId) ?: return@mapNotNull null
            CategoryAmountDetail(
                categoryId = categoryId,
                categoryName = category.name,
                categoryIcon = category.icon,
                categoryColor = category.color,
                amount = amount
            )
        }
    }

    /**
     * 获取每日收支总计
     *
     * @param type 交易类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 每日收支总计列表
     */
    suspend fun getDailyTotals(type: TransactionType, startDate: LocalDateTime, endDate: LocalDateTime): List<DailyTotalDetail> {
        val dailyTotals = transactionRepository.getDailyTotals(type, startDate, endDate)
        
        return dailyTotals.map { (date, amount) ->
            DailyTotalDetail(
                date = date,
                amount = amount
            )
        }
    }

    /**
     * 获取最近的交易记录
     *
     * @param limit 限制数量
     * @return 最近的交易记录列表
     */
    suspend fun getRecentTransactions(limit: Int = 10) = 
        transactionRepository.getAll().first().take(limit)

    /**
     * 获取支出最多的分类
     *
     * @param limit 限制数量
     * @return 支出最多的分类列表
     */
    suspend fun getTopExpenseCategories(limit: Int = 5): List<CategoryAmountDetail> {
        val startDate = LocalDateTime.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0)
        val endDate = LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59)

        return getCategoryBreakdown(TransactionType.EXPENSE, startDate, endDate)
            .sortedByDescending { it.amount }
            .take(limit)
    }
}

/**
 * 月度汇总数据
 *
 * @property totalIncome 总收入
 * @property totalExpense 总支出
 * @property balance 结余
 * @property startDate 开始日期
 * @property endDate 结束日期
 */
data class MonthlySummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)

/**
 * 年度汇总数据
 *
 * @property totalIncome 总收入
 * @property totalExpense 总支出
 * @property balance 结余
 * @property year 年份
 */
data class YearlySummary(
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double,
    val year: Int
)

/**
 * 分类金额明细
 *
 * @property categoryId 分类ID
 * @property categoryName 分类名称
 * @property categoryIcon 分类图标
 * @property categoryColor 分类颜色
 * @property amount 金额
 */
data class CategoryAmountDetail(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: String,
    val amount: Double
)

/**
 * 每日金额明细
 *
 * @property date 日期
 * @property amount 金额
 */
data class DailyTotalDetail(
    val date: String,
    val amount: Double
)
