package com.ledger.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.app.R
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.model.Transaction
import com.ledger.app.ui.components.TransactionCard
import com.ledger.app.ui.navigation.Screen
import com.ledger.app.utils.formatter.DateFormatter
import com.ledger.app.viewmodel.transaction.TransactionViewModel

@Composable
fun TransactionListScreen(
    onAddTransaction: () -> Unit,
    transactionViewModel: TransactionViewModel
) {
    val context = LocalContext.current
    val transactionState by transactionViewModel.state.collectAsState()
    
    // 加载交易数据
    LaunchedEffect(key1 = true) {
        transactionViewModel.loadTransactions()
    }
    
    // 显示交易列表
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = context.getString(R.string.transactions),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // TODO: Implement transaction list content
        // 1. Filter and search options
        // 2. Transaction list
        // 3. Empty state

        // 显示交易列表
        if (transactionState.transactions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(transactionState.transactions) { transaction ->
                    when (transaction) {
                        is Transaction.Income -> {
                            TransactionCard(
                                id = transaction.id,
                                amount = transaction.amount,
                                type = TransactionType.INCOME,
                                categoryName = transaction.categoryName,
                                categoryIcon = transaction.categoryIcon,
                                categoryColor = android.graphics.Color.parseColor(transaction.categoryColor),
                                date = DateFormatter.formatDate(transaction.date.toLocalDate()),
                                description = transaction.description,
                                onClick = { /* TODO: Navigate to transaction detail */ }
                            )
                        }
                        is Transaction.Expense -> {
                            TransactionCard(
                                id = transaction.id,
                                amount = transaction.amount,
                                type = TransactionType.EXPENSE,
                                categoryName = transaction.categoryName,
                                categoryIcon = transaction.categoryIcon,
                                categoryColor = android.graphics.Color.parseColor(transaction.categoryColor),
                                date = DateFormatter.formatDate(transaction.date.toLocalDate()),
                                description = transaction.description,
                                onClick = { /* TODO: Navigate to transaction detail */ }
                            )
                        }
                    }
                }
            }
        } else {
            // 空状态
            Text(
                text = context.getString(R.string.no_transactions),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
}
