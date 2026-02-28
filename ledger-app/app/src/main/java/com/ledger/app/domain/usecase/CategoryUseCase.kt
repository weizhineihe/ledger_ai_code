/**
 * 分类用例类
 * 处理分类相关的业务逻辑
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.domain.usecase

import com.ledger.app.data.entity.CategoryEntity
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.model.Category
import com.ledger.app.data.model.CategoryInput
import com.ledger.app.data.repository.CategoryRepository
import com.ledger.app.data.repository.TransactionRepository
import kotlinx.coroutines.flow.first

/**
 * 分类用例
 * 提供分类的创建、更新、删除、查询等操作
 *
 * @param categoryRepository 分类仓库
 * @param transactionRepository 交易仓库
 */
class CategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) {
    /**
     * 创建分类
     *
     * @param input 分类输入数据
     * @return 创建的分类ID
     * @throws IllegalArgumentException 当输入数据无效时
     */
    suspend fun createCategory(input: CategoryInput): Long {
        // 验证输入数据
        validateCategoryInput(input)

        // 创建分类实体
        val category = CategoryEntity(
            name = input.name,
            type = input.type,
            icon = input.icon,
            color = input.color,
            isDefault = false
        )

        return categoryRepository.insert(category)
    }

    /**
     * 更新分类
     *
     * @param id 分类ID
     * @param input 分类输入数据
     * @throws IllegalArgumentException 当输入数据无效或分类不存在时
     */
    suspend fun updateCategory(id: Long, input: CategoryInput) {
        // 验证输入数据
        validateCategoryInput(input)

        // 获取现有分类
        val existingCategory = categoryRepository.getById(id)
            ?: throw IllegalArgumentException("分类不存在")

        // 更新分类实体
        val updatedCategory = existingCategory.copy(
            name = input.name,
            icon = input.icon,
            color = input.color
        )

        categoryRepository.update(updatedCategory)
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @throws IllegalArgumentException 当分类不存在、是默认分类或被交易使用时
     */
    suspend fun deleteCategory(id: Long) {
        val category = categoryRepository.getById(id)
            ?: throw IllegalArgumentException("分类不存在")

        // 检查是否为默认分类
        if (category.isDefault) {
            throw IllegalArgumentException("不能删除默认分类")
        }

        // 检查分类是否被交易使用
        val transactions = transactionRepository.getByCategory(id)
        if (transactions.first().isNotEmpty()) {
            throw IllegalArgumentException("不能删除已被交易使用的分类")
        }

        categoryRepository.delete(category)
    }

    /**
     * 获取分类
     *
     * @param id 分类ID
     * @return 分类对象，不存在则返回null
     */
    suspend fun getCategory(id: Long): Category? {
        val categoryEntity = categoryRepository.getById(id) ?: return null
        return Category.fromEntity(categoryEntity)
    }

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    fun getAllCategories() = categoryRepository.getAll()

    /**
     * 获取收入分类
     *
     * @return 收入分类列表
     */
    fun getIncomeCategories() = categoryRepository.getIncomeCategories()

    /**
     * 获取支出分类
     *
     * @return 支出分类列表
     */
    fun getExpenseCategories() = categoryRepository.getExpenseCategories()

    /**
     * 初始化默认分类
     */
    suspend fun initializeDefaultCategories() {
        categoryRepository.initializeDefaultCategories()
    }

    /**
     * 验证分类输入数据
     *
     * @param input 分类输入数据
     * @throws IllegalArgumentException 当输入数据无效时
     */
    private fun validateCategoryInput(input: CategoryInput) {
        if (input.name.isBlank()) {
            throw IllegalArgumentException("分类名称不能为空")
        }

        if (input.icon.isBlank()) {
            throw IllegalArgumentException("分类图标不能为空")
        }

        if (input.color.isBlank()) {
            throw IllegalArgumentException("分类颜色不能为空")
        }
    }
}
