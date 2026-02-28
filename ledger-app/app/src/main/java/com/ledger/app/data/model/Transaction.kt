/**
 * 交易模型类
 * 
 * 用于UI层和业务逻辑层的交易数据模型
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.model

import com.ledger.app.data.entity.TransactionType
import java.time.LocalDateTime

/**
 * 交易密封类
 * 
 * 分为收入和支出两种类型
 */
sealed class Transaction {
    /**
     * 收入交易
     * 
     * @param id 交易ID
     * @param amount 金额
     * @param categoryId 分类ID
     * @param categoryName 分类名称
     * @param categoryIcon 分类图标
     * @param categoryColor 分类颜色
     * @param date 交易日期
     * @param description 交易描述
     */
    data class Income(
        val id: Long = 0,
        val amount: Double,
        val categoryId: Long,
        val categoryName: String,
        val categoryIcon: String,
        val categoryColor: String,
        val date: LocalDateTime,
        val description: String? = null
    ) : Transaction()

    /**
     * 支出交易
     * 
     * @param id 交易ID
     * @param amount 金额
     * @param categoryId 分类ID
     * @param categoryName 分类名称
     * @param categoryIcon 分类图标
     * @param categoryColor 分类颜色
     * @param date 交易日期
     * @param description 交易描述
     */
    data class Expense(
        val id: Long = 0,
        val amount: Double,
        val categoryId: Long,
        val categoryName: String,
        val categoryIcon: String,
        val categoryColor: String,
        val date: LocalDateTime,
        val description: String? = null
    ) : Transaction()

    companion object {
        /**
         * 从实体类转换为模型类
         * 
         * @param entity 交易实体
         * @param categoryName 分类名称
         * @param categoryIcon 分类图标
         * @param categoryColor 分类颜色
         * @return 交易模型
         */
        fun fromEntity(
            entity: com.ledger.app.data.entity.TransactionEntity,
            categoryName: String,
            categoryIcon: String,
            categoryColor: String
        ): Transaction {
            return when (entity.type) {
                TransactionType.INCOME -> Income(
                    id = entity.id,
                    amount = entity.amount,
                    categoryId = entity.categoryId,
                    categoryName = categoryName,
                    categoryIcon = categoryIcon,
                    categoryColor = categoryColor,
                    date = entity.date,
                    description = entity.description
                )
                TransactionType.EXPENSE -> Expense(
                    id = entity.id,
                    amount = entity.amount,
                    categoryId = entity.categoryId,
                    categoryName = categoryName,
                    categoryIcon = categoryIcon,
                    categoryColor = categoryColor,
                    date = entity.date,
                    description = entity.description
                )
            }
        }
    }
}

/**
 * 交易输入数据类
 * 
 * 用于创建或更新交易时的输入数据
 * 
 * @param amount 金额
 * @param type 交易类型
 * @param categoryId 分类ID
 * @param date 交易日期
 * @param description 交易描述
 */
data class TransactionInput(
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val date: LocalDateTime,
    val description: String? = null
)
