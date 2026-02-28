/**
 * 分类仓库类
 * 
 * 处理分类数据的业务逻辑，包括分类的增删改查和默认分类的初始化
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.repository

import com.ledger.app.data.dao.CategoryDao
import com.ledger.app.data.entity.CategoryEntity
import com.ledger.app.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import com.ledger.app.utils.common.LogUtils

/**
 * 分类仓库
 * 
 * 封装分类相关的数据库操作和业务逻辑
 */
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val categoryDao: CategoryDao?) {
    private val TAG = "CategoryRepository"
    
    /**
     * 备用构造函数，用于防止Hilt注入失败时应用崩溃
     */
    constructor() : this(null)
    
    /**
     * 插入分类
     * 
     * @param category 分类实体
     * @return 插入的分类ID
     */
    suspend fun insert(category: CategoryEntity): Long {
        LogUtils.d(TAG, "Inserting category: $category")
        val id = categoryDao?.insert(category) ?: 0
        LogUtils.d(TAG, "Inserted category with id: $id")
        return id
    }

    /**
     * 更新分类
     * 
     * @param category 分类实体
     */
    suspend fun update(category: CategoryEntity) {
        LogUtils.d(TAG, "Updating category: $category")
        categoryDao?.update(category)
        LogUtils.d(TAG, "Updated category successfully")
    }

    /**
     * 删除分类
     * 
     * @param category 分类实体
     */
    suspend fun delete(category: CategoryEntity) {
        LogUtils.d(TAG, "Deleting category: $category")
        categoryDao?.delete(category)
        LogUtils.d(TAG, "Deleted category successfully")
    }

    /**
     * 根据ID获取分类
     * 
     * @param id 分类ID
     * @return 分类实体，不存在则返回null
     */
    suspend fun getById(id: Long): CategoryEntity? {
        LogUtils.d(TAG, "Getting category by id: $id")
        val category = categoryDao?.getById(id)
        LogUtils.d(TAG, "Got category: $category")
        return category
    }
    
    /**
     * 根据ID列表批量获取分类
     * 
     * @param ids 分类ID列表
     * @return 分类实体列表
     */
    suspend fun getByIds(ids: List<Long>): List<CategoryEntity> {
        LogUtils.d(TAG, "Getting categories by ids: $ids")
        val categories = categoryDao?.getByIds(ids) ?: emptyList()
        LogUtils.d(TAG, "Got categories: $categories")
        return categories
    }

    /**
     * 获取所有分类
     * 
     * @return 分类列表流
     */
    fun getAll(): Flow<List<CategoryEntity>> {
        LogUtils.d(TAG, "Getting all categories")
        return categoryDao?.getAll() ?: kotlinx.coroutines.flow.emptyFlow()
    }

    /**
     * 获取收入分类
     * 
     * @return 收入分类列表流
     */
    fun getIncomeCategories(): Flow<List<CategoryEntity>> {
        LogUtils.d(TAG, "Getting income categories")
        return categoryDao?.getByType(TransactionType.INCOME) ?: kotlinx.coroutines.flow.emptyFlow()
    }

    /**
     * 获取支出分类
     * 
     * @return 支出分类列表流
     */
    fun getExpenseCategories(): Flow<List<CategoryEntity>> {
        LogUtils.d(TAG, "Getting expense categories")
        return categoryDao?.getByType(TransactionType.EXPENSE) ?: kotlinx.coroutines.flow.emptyFlow()
    }

    /**
     * 统计收入分类数量
     * 
     * @return 收入分类数量
     */
    suspend fun countIncomeCategories(): Int {
        LogUtils.d(TAG, "Counting income categories")
        val count = categoryDao?.countByType(TransactionType.INCOME) ?: 0
        LogUtils.d(TAG, "Income categories count: $count")
        return count
    }

    /**
     * 统计支出分类数量
     * 
     * @return 支出分类数量
     */
    suspend fun countExpenseCategories(): Int {
        LogUtils.d(TAG, "Counting expense categories")
        val count = categoryDao?.countByType(TransactionType.EXPENSE) ?: 0
        LogUtils.d(TAG, "Expense categories count: $count")
        return count
    }

    /**
     * 初始化默认分类
     * 
     * 如果没有收入或支出分类，则插入默认分类
     */
    suspend fun initializeDefaultCategories() {
        LogUtils.d(TAG, "Initializing default categories")
        if (categoryDao == null) {
            LogUtils.w(TAG, "CategoryDao is null, skipping initialization")
            return
        }
        
        // Check if we need to initialize default categories
        if (countIncomeCategories() == 0) {
            LogUtils.d(TAG, "No income categories found, inserting default income categories")
            // Insert default income categories
            val defaultIncomeCategories = listOf(
                CategoryEntity(
                    name = "Salary",
                    type = TransactionType.INCOME,
                    icon = "account_balance_wallet",
                    color = "#4CAF50",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Bonus",
                    type = TransactionType.INCOME,
                    icon = "card_giftcard",
                    color = "#2196F3",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Investment",
                    type = TransactionType.INCOME,
                    icon = "trending_up",
                    color = "#FF9800",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Other Income",
                    type = TransactionType.INCOME,
                    icon = "attach_money",
                    color = "#9C27B0",
                    isDefault = true
                )
            )
            defaultIncomeCategories.forEach { 
                val id = categoryDao.insert(it)
                LogUtils.d(TAG, "Inserted default income category: ${it.name} with id: $id")
            }
        } else {
            LogUtils.d(TAG, "Income categories already exist, skipping initialization")
        }

        if (countExpenseCategories() == 0) {
            LogUtils.d(TAG, "No expense categories found, inserting default expense categories")
            // Insert default expense categories
            val defaultExpenseCategories = listOf(
                CategoryEntity(
                    name = "Food",
                    type = TransactionType.EXPENSE,
                    icon = "restaurant",
                    color = "#F44336",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Transportation",
                    type = TransactionType.EXPENSE,
                    icon = "directions_car",
                    color = "#2196F3",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Shopping",
                    type = TransactionType.EXPENSE,
                    icon = "shopping_cart",
                    color = "#9C27B0",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Entertainment",
                    type = TransactionType.EXPENSE,
                    icon = "movie",
                    color = "#FF9800",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Housing",
                    type = TransactionType.EXPENSE,
                    icon = "home",
                    color = "#4CAF50",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Utilities",
                    type = TransactionType.EXPENSE,
                    icon = "flash_on",
                    color = "#00BCD4",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Healthcare",
                    type = TransactionType.EXPENSE,
                    icon = "local_hospital",
                    color = "#E91E63",
                    isDefault = true
                ),
                CategoryEntity(
                    name = "Other Expense",
                    type = TransactionType.EXPENSE,
                    icon = "more_horiz",
                    color = "#607D8B",
                    isDefault = true
                )
            )
            defaultExpenseCategories.forEach { 
                val id = categoryDao.insert(it)
                LogUtils.d(TAG, "Inserted default expense category: ${it.name} with id: $id")
            }
        } else {
            LogUtils.d(TAG, "Expense categories already exist, skipping initialization")
        }
        LogUtils.d(TAG, "Default categories initialization completed")
    }
}
