package com.ledger.app.viewmodel.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.app.data.entity.CategoryEntity
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing category-related operations and state
 * 
 * This ViewModel handles:
 * - Loading categories by type
 * - Initializing default categories
 * - Adding, updating, and deleting custom categories
 * - Checking category name uniqueness
 */
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    /**
     * Get all income categories
     * 
     * @return Flow of list of income categories
     */
    fun getIncomeCategories(): Flow<List<CategoryEntity>> {
        // 初始化默认分类（如果需要）
        initializeDefaultCategories()
        return categoryRepository.getIncomeCategories()
    }

    /**
     * Get all expense categories
     * 
     * @return Flow of list of expense categories
     */
    fun getExpenseCategories(): Flow<List<CategoryEntity>> {
        // 初始化默认分类（如果需要）
        initializeDefaultCategories()
        return categoryRepository.getExpenseCategories()
    }

    /**
     * Initialize default categories if none exist
     */
    fun initializeDefaultCategories() {
        // 在后台线程初始化默认分类
        viewModelScope.launch {
            categoryRepository.initializeDefaultCategories()
        }
    }

    /**
     * Add a new custom category
     * 
     * @param category Category entity to add
     * @return ID of the added category
     */
    fun addCategory(category: CategoryEntity): Long {
        var categoryId = 0L
        viewModelScope.launch {
            categoryId = categoryRepository.insert(category)
        }
        return categoryId
    }

    /**
     * Update an existing category
     * 
     * @param category Category entity to update
     */
    fun updateCategory(category: CategoryEntity) {
        viewModelScope.launch {
            categoryRepository.update(category)
        }
    }

    /**
     * Delete a category
     * 
     * @param category Category entity to delete
     */
    fun deleteCategory(category: CategoryEntity) {
        // 只允许删除自定义分类
        if (!category.isDefault) {
            viewModelScope.launch {
                categoryRepository.delete(category)
            }
        }
    }

    /**
     * Check if a category name is unique
     * 
     * @param name Category name to check
     * @param type Transaction type
     * @param excludeId ID of category to exclude from check (for updates)
     * @return Boolean indicating if the name is unique
     */
    suspend fun isCategoryNameUnique(name: String, type: TransactionType, excludeId: Long? = null): Boolean {
        // 这里需要实现分类名称唯一性检查
        // 暂时返回true，实际实现中需要查询数据库
        return true
    }
}
