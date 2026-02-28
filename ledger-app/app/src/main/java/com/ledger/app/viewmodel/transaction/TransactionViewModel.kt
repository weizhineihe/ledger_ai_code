package com.ledger.app.viewmodel.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.app.data.model.Transaction
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.model.TransactionInput
import com.ledger.app.domain.usecase.TransactionUseCase
import com.ledger.app.utils.common.LogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

/**
 * ViewModel for managing transaction-related operations and state
 * 
 * This ViewModel handles:
 * - Loading transactions from different sources (all, date range, type, search)
 * - Creating, updating, and deleting transactions
 * - Managing UI state for transaction operations
 */
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {
    private val TAG = "TransactionViewModel"

    /**
     * State flow for transaction-related UI state
     */
    private val _state = MutableStateFlow(TransactionState())
    val state: StateFlow<TransactionState> = _state.asStateFlow()

    /**
     * Load all transactions
     * 
     * This function fetches all transactions from the repository and updates the state
     */
    fun loadTransactions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val transactions = transactionUseCase.getAllTransactions()
                transactions.collect {
                    // Calculate total income and expense
                    val totalIncome = it.filterIsInstance<Transaction.Income>().sumOf { it.amount }
                    val totalExpense = it.filterIsInstance<Transaction.Expense>().sumOf { it.amount }
                    
                    _state.update {
                        state -> state.copy(
                            isLoading = false,
                            transactions = it,
                            totalIncome = totalIncome,
                            totalExpense = totalExpense
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /**
     * Load transactions by date range
     * 
     * @param startDate Start date of the range
     * @param endDate End date of the range
     */
    fun loadTransactionsByDateRange(startDate: LocalDateTime, endDate: LocalDateTime) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val transactions = transactionUseCase.getTransactionsByDateRange(startDate, endDate)
                transactions.collect {
                    // Calculate total income and expense
                    val totalIncome = it.filterIsInstance<Transaction.Income>().sumOf { it.amount }
                    val totalExpense = it.filterIsInstance<Transaction.Expense>().sumOf { it.amount }
                    
                    _state.update {
                        state -> state.copy(
                            isLoading = false,
                            transactions = it,
                            totalIncome = totalIncome,
                            totalExpense = totalExpense
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /**
     * Load transactions by year
     * 
     * @param year Target year
     */
    fun loadTransactionsByYear(year: Int) {
        val startDate = LocalDateTime.of(year, 1, 1, 0, 0, 0)
        val endDate = LocalDateTime.of(year, 12, 31, 23, 59, 59)
        loadTransactionsByDateRange(startDate, endDate)
    }

    /**
     * Load transactions by year and month
     * 
     * @param year Target year
     * @param month Target month (1-12)
     */
    fun loadTransactionsByMonth(year: Int, month: Int) {
        val startDate = LocalDateTime.of(year, month, 1, 0, 0, 0)
        val endDate = LocalDateTime.of(year, month, YearMonth.of(year, month).lengthOfMonth(), 23, 59, 59)
        loadTransactionsByDateRange(startDate, endDate)
    }

    /**
     * Load transactions by type
     * 
     * @param type Transaction type (INCOME or EXPENSE)
     */
    fun loadTransactionsByType(type: TransactionType) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val transactions = transactionUseCase.getTransactionsByType(type)
                transactions.collect {
                    _state.update {
                        state -> state.copy(
                            isLoading = false,
                            transactions = it
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /**
     * Search transactions by query
     * 
     * @param query Search query string
     */
    fun searchTransactions(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val transactions = transactionUseCase.searchTransactions(query)
                transactions.collect {
                    _state.update {
                        state -> state.copy(
                            isLoading = false,
                            transactions = it
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /**
     * Create a new transaction
     * 
     * @param input Transaction input data
     */
    fun createTransaction(input: TransactionInput) {
        LogUtils.d(TAG, "Creating transaction: $input")
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            try {
                LogUtils.d(TAG, "Calling transactionUseCase.createTransaction")
                val transactionId = transactionUseCase.createTransaction(input)
                LogUtils.d(TAG, "Transaction created successfully with id: $transactionId")
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        isSuccess = true
                    )
                }
                LogUtils.d(TAG, "Transaction state updated successfully")
            } catch (e: Exception) {
                LogUtils.e(TAG, "Failed to create transaction: ${e.message}", e)
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        error = e.message
                    )
                }
                LogUtils.d(TAG, "Transaction state updated with error: ${e.message}")
            }
        }
    }

    /**
     * Update an existing transaction
     * 
     * @param id Transaction ID
     * @param input Updated transaction data
     */
    fun updateTransaction(id: Long, input: TransactionInput) {
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            try {
                transactionUseCase.updateTransaction(id, input)
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /**
     * Delete a transaction
     * 
     * @param id Transaction ID to delete
     */
    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }

            try {
                transactionUseCase.deleteTransaction(id)
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSubmitting = false,
                        error = e.message
                    )
                }
            }
        }
    }

    /**
     * Reset success and error states
     * 
     * This is useful after handling a success or error event in the UI
     */
    fun resetState() {
        _state.update {
            it.copy(
                isSuccess = false,
                error = null
            )
        }
    }

    /**
     * State class for transaction operations
     * 
     * @property isLoading Whether transactions are currently being loaded
     * @property isSubmitting Whether a transaction operation is being submitted
     * @property isSuccess Whether the last operation was successful
     * @property error Error message if any operation failed
     * @property transactions List of loaded transactions
     * @property totalIncome Total income amount
     * @property totalExpense Total expense amount
     */
    data class TransactionState(
        val isLoading: Boolean = false,
        val isSubmitting: Boolean = false,
        val isSuccess: Boolean = false,
        val error: String? = null,
        val transactions: List<Transaction> = emptyList(),
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0
    )
}
