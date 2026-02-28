package com.ledger.app.domain.usecase

import com.ledger.app.data.entity.TransactionEntity
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.model.Transaction
import com.ledger.app.data.model.TransactionInput
import com.ledger.app.data.repository.CategoryRepository
import com.ledger.app.data.repository.TransactionRepository
import com.ledger.app.utils.common.LogUtils
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

class TransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository
) {
    private val TAG = "TransactionUseCase"
    suspend fun createTransaction(input: TransactionInput): Long {
        LogUtils.d(TAG, "Creating transaction: $input")
        
        // Validate input
        LogUtils.d(TAG, "Validating transaction input")
        validateTransactionInput(input)
        LogUtils.d(TAG, "Transaction input validation passed")

        // Create transaction entity
        LogUtils.d(TAG, "Creating transaction entity")
        val transaction = TransactionEntity(
            amount = input.amount,
            type = input.type,
            categoryId = input.categoryId,
            date = input.date,
            description = input.description
        )
        LogUtils.d(TAG, "Created transaction entity: $transaction")

        // Insert transaction
        LogUtils.d(TAG, "Inserting transaction into repository")
        val transactionId = transactionRepository.insert(transaction)
        LogUtils.d(TAG, "Transaction inserted successfully with id: $transactionId")

        return transactionId
    }

    suspend fun updateTransaction(id: Long, input: TransactionInput) {
        // Validate input
        validateTransactionInput(input)

        // Get existing transaction
        val existingTransaction = transactionRepository.getById(id)
            ?: throw IllegalArgumentException("Transaction not found")

        // Update transaction entity
        val updatedTransaction = existingTransaction.copy(
            amount = input.amount,
            type = input.type,
            categoryId = input.categoryId,
            date = input.date,
            description = input.description,
            updatedAt = LocalDateTime.now()
        )

        transactionRepository.update(updatedTransaction)
    }

    suspend fun deleteTransaction(id: Long) {
        val transaction = transactionRepository.getById(id)
            ?: throw IllegalArgumentException("Transaction not found")

        transactionRepository.delete(transaction)
    }

    suspend fun getTransaction(id: Long): Transaction? {
        val transactionEntity = transactionRepository.getById(id) ?: return null
        val categoryEntity = categoryRepository.getById(transactionEntity.categoryId) ?: return null

        return Transaction.fromEntity(
            entity = transactionEntity,
            categoryName = categoryEntity.name,
            categoryIcon = categoryEntity.icon,
            categoryColor = categoryEntity.color
        )
    }

    fun getAllTransactions(): kotlinx.coroutines.flow.Flow<List<Transaction>> = transactionRepository.getAll().map { transactionEntities ->
        // 批量获取所有需要的分类信息
        val categoryIds = transactionEntities.map { it.categoryId }.distinct()
        val categoryMap = categoryRepository.getByIds(categoryIds).associateBy { it.id }
        
        transactionEntities.mapNotNull { transactionEntity ->
            val categoryEntity = categoryMap[transactionEntity.categoryId]
            if (categoryEntity != null) {
                Transaction.fromEntity(
                    entity = transactionEntity,
                    categoryName = categoryEntity.name,
                    categoryIcon = categoryEntity.icon,
                    categoryColor = categoryEntity.color
                )
            } else {
                null
            }
        }
    }

    fun getTransactionsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): kotlinx.coroutines.flow.Flow<List<Transaction>> = 
        transactionRepository.getByDateRange(startDate, endDate).map { transactionEntities ->
            // 批量获取所有需要的分类信息
            val categoryIds = transactionEntities.map { it.categoryId }.distinct()
            val categoryMap = categoryRepository.getByIds(categoryIds).associateBy { it.id }
            
            transactionEntities.mapNotNull { transactionEntity ->
                val categoryEntity = categoryMap[transactionEntity.categoryId]
                if (categoryEntity != null) {
                    Transaction.fromEntity(
                        entity = transactionEntity,
                        categoryName = categoryEntity.name,
                        categoryIcon = categoryEntity.icon,
                        categoryColor = categoryEntity.color
                    )
                } else {
                    null
                }
            }
        }

    fun getTransactionsByCategory(categoryId: Long): kotlinx.coroutines.flow.Flow<List<Transaction>> = 
        transactionRepository.getByCategory(categoryId).map { transactionEntities ->
            // 批量获取所有需要的分类信息
            val categoryIds = transactionEntities.map { it.categoryId }.distinct()
            val categoryMap = categoryRepository.getByIds(categoryIds).associateBy { it.id }
            
            transactionEntities.mapNotNull { transactionEntity ->
                val categoryEntity = categoryMap[transactionEntity.categoryId]
                if (categoryEntity != null) {
                    Transaction.fromEntity(
                        entity = transactionEntity,
                        categoryName = categoryEntity.name,
                        categoryIcon = categoryEntity.icon,
                        categoryColor = categoryEntity.color
                    )
                } else {
                    null
                }
            }
        }

    fun getTransactionsByType(type: TransactionType): kotlinx.coroutines.flow.Flow<List<Transaction>> = 
        transactionRepository.getByType(type).map { transactionEntities ->
            // 批量获取所有需要的分类信息
            val categoryIds = transactionEntities.map { it.categoryId }.distinct()
            val categoryMap = categoryRepository.getByIds(categoryIds).associateBy { it.id }
            
            transactionEntities.mapNotNull { transactionEntity ->
                val categoryEntity = categoryMap[transactionEntity.categoryId]
                if (categoryEntity != null) {
                    Transaction.fromEntity(
                        entity = transactionEntity,
                        categoryName = categoryEntity.name,
                        categoryIcon = categoryEntity.icon,
                        categoryColor = categoryEntity.color
                    )
                } else {
                    null
                }
            }
        }

    fun searchTransactions(query: String): kotlinx.coroutines.flow.Flow<List<Transaction>> = 
        transactionRepository.search(query).map { transactionEntities ->
            // 批量获取所有需要的分类信息
            val categoryIds = transactionEntities.map { it.categoryId }.distinct()
            val categoryMap = categoryRepository.getByIds(categoryIds).associateBy { it.id }
            
            transactionEntities.mapNotNull { transactionEntity ->
                val categoryEntity = categoryMap[transactionEntity.categoryId]
                if (categoryEntity != null) {
                    Transaction.fromEntity(
                        entity = transactionEntity,
                        categoryName = categoryEntity.name,
                        categoryIcon = categoryEntity.icon,
                        categoryColor = categoryEntity.color
                    )
                } else {
                    null
                }
            }
        }

    private suspend fun validateTransactionInput(input: TransactionInput) {
        LogUtils.d(TAG, "Validating transaction input: $input")
        
        // Validate amount
        LogUtils.d(TAG, "Validating amount: ${input.amount}")
        if (input.amount <= 0) {
            LogUtils.e(TAG, "Amount validation failed: Amount must be positive")
            throw IllegalArgumentException("Amount must be positive")
        }
        LogUtils.d(TAG, "Amount validation passed")

        // Validate category exists and matches transaction type
        LogUtils.d(TAG, "Validating category with id: ${input.categoryId}")
        val category = categoryRepository.getById(input.categoryId)
        if (category == null) {
            LogUtils.e(TAG, "Category validation failed: Category not found")
            throw IllegalArgumentException("Category not found")
        }
        LogUtils.d(TAG, "Found category: $category")
        
        if (category.type != input.type) {
            LogUtils.e(TAG, "Category type validation failed: Category type ${category.type} does not match transaction type ${input.type}")
            throw IllegalArgumentException("Category type does not match transaction type")
        }
        LogUtils.d(TAG, "Category type validation passed")
        LogUtils.d(TAG, "All validations passed")
    }
}
