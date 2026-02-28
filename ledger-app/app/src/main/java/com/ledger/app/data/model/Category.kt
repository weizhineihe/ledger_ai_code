/**
 * 分类模型类
 * 
 * 用于UI层和业务逻辑层的分类数据模型
 * 
 * 创建日期: 2026-02-12
 */
package com.ledger.app.data.model

import com.ledger.app.data.entity.CategoryEntity
import com.ledger.app.data.entity.TransactionType

/**
 * 分类密封类
 * 
 * 分为收入分类和支出分类两种类型
 */
sealed class Category {
    /**
     * 收入分类
     * 
     * @param id 分类ID
     * @param name 分类名称
     * @param icon 分类图标
     * @param color 分类颜色
     * @param isDefault 是否为默认分类
     */
    data class IncomeCategory(
        val id: Long = 0,
        val name: String,
        val icon: String,
        val color: String,
        val isDefault: Boolean = false
    ) : Category()

    /**
     * 支出分类
     * 
     * @param id 分类ID
     * @param name 分类名称
     * @param icon 分类图标
     * @param color 分类颜色
     * @param isDefault 是否为默认分类
     */
    data class ExpenseCategory(
        val id: Long = 0,
        val name: String,
        val icon: String,
        val color: String,
        val isDefault: Boolean = false
    ) : Category()

    companion object {
        /**
         * 从实体类转换为模型类
         * 
         * @param entity 分类实体
         * @return 分类模型
         */
        fun fromEntity(entity: CategoryEntity): Category {
            return when (entity.type) {
                TransactionType.INCOME -> IncomeCategory(
                    id = entity.id,
                    name = entity.name,
                    icon = entity.icon,
                    color = entity.color,
                    isDefault = entity.isDefault
                )
                TransactionType.EXPENSE -> ExpenseCategory(
                    id = entity.id,
                    name = entity.name,
                    icon = entity.icon,
                    color = entity.color,
                    isDefault = entity.isDefault
                )
            }
        }

        /**
         * 从模型类转换为实体类
         * 
         * @param category 分类模型
         * @return 分类实体
         */
        fun toEntity(category: Category): CategoryEntity {
            return when (category) {
                is IncomeCategory -> CategoryEntity(
                    id = category.id,
                    name = category.name,
                    type = TransactionType.INCOME,
                    icon = category.icon,
                    color = category.color,
                    isDefault = category.isDefault
                )
                is ExpenseCategory -> CategoryEntity(
                    id = category.id,
                    name = category.name,
                    type = TransactionType.EXPENSE,
                    icon = category.icon,
                    color = category.color,
                    isDefault = category.isDefault
                )
            }
        }
    }
}

/**
 * 分类输入数据类
 * 
 * 用于创建或更新分类时的输入数据
 * 
 * @param name 分类名称
 * @param type 分类类型
 * @param icon 分类图标
 * @param color 分类颜色，默认紫色
 */
data class CategoryInput(
    val name: String,
    val type: TransactionType,
    val icon: String,
    val color: String = "#FF6200EE"
)
