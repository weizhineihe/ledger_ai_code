/**
 * 账本数据库类
 * 
 * 定义应用的数据库配置和类型转换器
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ledger.app.data.dao.CategoryDao
import com.ledger.app.data.dao.TransactionDao
import com.ledger.app.data.entity.CategoryEntity
import com.ledger.app.data.entity.TransactionEntity
import com.ledger.app.data.entity.TransactionType
import java.time.LocalDateTime

/**
 * 账本数据库
 * 
 * 配置应用的Room数据库，包含交易和分类表
 */
@Database(
    entities = [TransactionEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(LedgerTypeConverters::class)
abstract class LedgerDatabase : RoomDatabase() {
    /**
     * 获取交易DAO
     * 
     * @return 交易数据访问对象
     */
    abstract fun transactionDao(): TransactionDao
    
    /**
     * 获取分类DAO
     * 
     * @return 分类数据访问对象
     */
    abstract fun categoryDao(): CategoryDao
}

/**
 * 账本类型转换器
 * 
 * 为Room数据库提供类型转换功能
 */
class LedgerTypeConverters {
    /**
     * 将LocalDateTime转换为字符串
     * 
     * @param value LocalDateTime对象
     * @return 字符串表示
     */
    @androidx.room.TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.toString()
    }

    /**
     * 将字符串转换为LocalDateTime
     * 
     * @param value 字符串表示
     * @return LocalDateTime对象
     */
    @androidx.room.TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    /**
     * 将TransactionType转换为字符串
     * 
     * @param value TransactionType枚举
     * @return 字符串表示
     */
    @androidx.room.TypeConverter
    fun fromTransactionType(value: TransactionType?): String? {
        return value?.name
    }

    /**
     * 将字符串转换为TransactionType
     * 
     * @param value 字符串表示
     * @return TransactionType枚举
     */
    @androidx.room.TypeConverter
    fun toTransactionType(value: String?): TransactionType? {
        return value?.let { TransactionType.valueOf(it) }
    }
}
