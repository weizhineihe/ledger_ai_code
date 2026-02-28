/**
 * 分类实体类
 * 
 * 用于存储交易分类信息，包括收入和支出分类
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 分类实体
 * 
 * 对应数据库中的categories表
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    /** 分类ID，自增主键 */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /** 分类名称 */
    val name: String,
    /** 分类类型，收入或支出 */
    val type: TransactionType,
    /** 分类图标名称 */
    val icon: String,
    /** 分类颜色，默认紫色 */
    val color: String = "#FF6200EE",
    /** 是否为默认分类 */
    val isDefault: Boolean = false
)
