/**
 * 交易实体类
 * 
 * 用于存储交易记录信息，包括收入和支出交易
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * 交易实体
 * 
 * 对应数据库中的transactions表
 */
@Entity(tableName = "transactions")
data class TransactionEntity(
    /** 交易ID，自增主键 */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 交易金额 */
    val amount: Double,
    /** 交易类型，收入或支出 */
    val type: TransactionType,
    /** 分类ID，关联categories表 */
    val categoryId: Long,
    /** 交易日期时间 */
    val date: LocalDateTime,
    /** 交易描述 */
    val description: String? = null,
    /** 创建时间 */
    val createdAt: LocalDateTime = LocalDateTime.now(),
    /** 更新时间 */
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * 交易类型枚举
 * 
 * INCOME: 收入
 * EXPENSE: 支出
 */
enum class TransactionType {
    INCOME,
    EXPENSE
}
