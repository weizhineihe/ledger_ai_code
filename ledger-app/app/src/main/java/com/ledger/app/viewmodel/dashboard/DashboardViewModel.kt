package com.ledger.app.viewmodel.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledger.app.domain.usecase.StatisticsUseCase
import com.ledger.app.domain.usecase.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val statisticsUseCase: StatisticsUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val currentMonth = YearMonth.now()
                val startOfMonth = currentMonth.atDay(1).atStartOfDay()
                val endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59)

                // Load monthly summary
                val monthlySummary = statisticsUseCase.getMonthlySummary(
                    currentMonth.year,
                    currentMonth.monthValue
                )

                // Load recent transactions
                val recentTransactions = statisticsUseCase.getRecentTransactions()

                // Load top expense categories
                val topExpenseCategories = statisticsUseCase.getTopExpenseCategories()

                _state.update {
                    it.copy(
                        isLoading = false,
                        monthlySummary = monthlySummary,
                        recentTransactions = recentTransactions,
                        topExpenseCategories = topExpenseCategories
                    )
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

    data class DashboardState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val monthlySummary: com.ledger.app.domain.usecase.MonthlySummary? = null,
        val recentTransactions: List<com.ledger.app.data.entity.TransactionEntity> = emptyList(),
        val topExpenseCategories: List<com.ledger.app.domain.usecase.CategoryAmountDetail> = emptyList()
    )
}
