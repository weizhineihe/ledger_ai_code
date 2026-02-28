package com.ledger.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.app.R
import com.ledger.app.data.entity.OverviewMode
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.model.Transaction
import com.ledger.app.ui.components.TransactionCard
import com.ledger.app.utils.formatter.DateFormatter
import com.ledger.app.viewmodel.transaction.TransactionViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import java.time.Year
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    transactionViewModel: TransactionViewModel
) {
    val context = LocalContext.current
    
    // 概览模式状态
    var overviewMode by remember { mutableStateOf(OverviewMode.ALL) }
    
    // 年份和月份选择状态
    var selectedYear by remember { mutableStateOf(Year.now().value) }
    var selectedMonth by remember { mutableStateOf(YearMonth.now().monthValue) }
    
    // 下拉菜单状态
    var showYearDropdown by remember { mutableStateOf(false) }
    var showMonthDropdown by remember { mutableStateOf(false) }
    
    // 加载状态，用于防连点
    var isLoading by remember { mutableStateOf(false) }
    
    // 加载概览数据（根据选择的年份和月份）
    fun loadOverviewData() {
        println("loadOverviewData called, isLoading: $isLoading")
        if (isLoading) {
            println("loadOverviewData skipped due to loading in progress")
            return // 防连点
        }
        
        println("Starting to load overview data")
        isLoading = true
        try {
            println("Loading overview data for mode: $overviewMode, year: $selectedYear, month: $selectedMonth")
            when (overviewMode) {
                OverviewMode.ALL -> {
                    println("Loading all transactions")
                    transactionViewModel.loadTransactions()
                }
                OverviewMode.YEARLY -> {
                    println("Loading transactions by year: $selectedYear")
                    transactionViewModel.loadTransactionsByYear(selectedYear)
                }
                OverviewMode.MONTHLY -> {
                    println("Loading transactions by month: $selectedMonth, year: $selectedYear")
                    transactionViewModel.loadTransactionsByMonth(selectedYear, selectedMonth)
                }
            }
        } catch (e: Exception) {
            println("Error loading overview data: ${e.message}")
        } finally {
            isLoading = false
            println("loadOverviewData completed, isLoading set to false")
        }
    }
    
    // 重置下拉框状态
    fun resetDropdownStates() {
        showYearDropdown = false
        showMonthDropdown = false
    }
    
    // 加载所有交易数据（用于最近交易列表）
    LaunchedEffect(Unit) {
        println("Initializing dashboard data")
        transactionViewModel.loadTransactions()
    }
    val allTransactions by transactionViewModel.state.collectAsState()
    
    // 初始加载概览数据
    LaunchedEffect(overviewMode, selectedYear, selectedMonth) {
        println("Loading overview data due to dependency change")
        loadOverviewData()
    }
    val overviewData by transactionViewModel.state.collectAsState()
    
    // 生成年份列表（最近5年）
    val years = (Year.now().value - 4..Year.now().value).toList().reversed()
    
    // 月份名称
    val monthNames = listOf(
        "1月", "2月", "3月", "4月", "5月", "6月",
        "7月", "8月", "9月", "10月", "11月", "12月"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = context.getString(R.string.dashboard),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // 财务概览卡片
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = context.getString(R.string.financial_overview),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // 概览模式切换按钮
            Column(modifier = Modifier.padding(top = 8.dp)) {
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            println("Overview mode changed to ALL")
                            overviewMode = OverviewMode.ALL
                            resetDropdownStates()
                            println("Mode changed, LaunchedEffect will handle data loading")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (overviewMode == OverviewMode.ALL) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    ) {
                        Text(context.getString(R.string.all))
                    }
                    Button(
                        onClick = {
                            println("Overview mode changed to YEARLY")
                            overviewMode = OverviewMode.YEARLY
                            resetDropdownStates()
                            println("Mode changed, LaunchedEffect will handle data loading")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (overviewMode == OverviewMode.YEARLY) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    ) {
                        Text(context.getString(R.string.yearly))
                    }
                    Button(
                        onClick = {
                            println("Overview mode changed to MONTHLY")
                            overviewMode = OverviewMode.MONTHLY
                            resetDropdownStates()
                            println("Mode changed, LaunchedEffect will handle data loading")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (overviewMode == OverviewMode.MONTHLY) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    ) {
                        Text(context.getString(R.string.monthly))
                    }
                }
                
                // 年份和月份选择器
                if (overviewMode == OverviewMode.YEARLY || overviewMode == OverviewMode.MONTHLY) {
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        // 年份选择
                        if (overviewMode == OverviewMode.YEARLY || overviewMode == OverviewMode.MONTHLY) {
                            ExposedDropdownMenuBox(
                                expanded = showYearDropdown,
                                onExpandedChange = { 
                                    println("Year dropdown expanded: $it")
                                    showYearDropdown = it
                                    // 确保月份下拉框关闭
                                    if (it) {
                                        println("Closing month dropdown")
                                        showMonthDropdown = false
                                    }
                                }
                            ) {
                                TextField(
                                    value = selectedYear.toString(),
                                    onValueChange = {},
                                    label = { Text(context.getString(R.string.year)) },
                                    modifier = Modifier
                                        .width(120.dp)
                                        .menuAnchor(),
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showYearDropdown) }
                                )
                                
                                ExposedDropdownMenu(
                                    expanded = showYearDropdown,
                                    onDismissRequest = { 
                                        println("Year dropdown dismissed")
                                        showYearDropdown = false 
                                    }
                                ) {
                                    years.forEach { year ->
                                        DropdownMenuItem(
                                            text = { Text(year.toString()) },
                                            onClick = {
                                                println("Year selected: $year")
                                                selectedYear = year
                                                showYearDropdown = false
                                                println("Year selected, LaunchedEffect will handle data loading")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        // 月份选择
                        if (overviewMode == OverviewMode.MONTHLY) {
                            ExposedDropdownMenuBox(
                                expanded = showMonthDropdown,
                                onExpandedChange = { 
                                    println("Month dropdown expanded: $it")
                                    showMonthDropdown = it
                                    // 确保年份下拉框关闭
                                    if (it) {
                                        println("Closing year dropdown")
                                        showYearDropdown = false
                                    }
                                }
                            ) {
                                TextField(
                                    value = monthNames[selectedMonth - 1],
                                    onValueChange = {},
                                    label = { Text(context.getString(R.string.month)) },
                                    modifier = Modifier
                                        .width(120.dp)
                                        .menuAnchor(),
                                    readOnly = true,
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMonthDropdown) }
                                )
                                
                                ExposedDropdownMenu(
                                    expanded = showMonthDropdown,
                                    onDismissRequest = { 
                                        println("Month dropdown dismissed")
                                        showMonthDropdown = false 
                                    }
                                ) {
                                    monthNames.forEachIndexed { index, monthName ->
                                        DropdownMenuItem(
                                            text = { Text(monthName) },
                                            onClick = {
                                                val selectedMonthValue = index + 1
                                                println("Month selected: $selectedMonthValue ($monthName)")
                                                selectedMonth = selectedMonthValue
                                                showMonthDropdown = false
                                                println("Month selected, LaunchedEffect will handle data loading")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // 收入和支出卡片
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Card(
                    modifier = Modifier.width(200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = context.getString(R.string.income),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "¥${String.format("%.2f", overviewData.totalIncome)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                Card(
                    modifier = Modifier.width(200.dp).padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = context.getString(R.string.expense),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "¥${String.format("%.2f", overviewData.totalExpense)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // 最近交易列表
        Column(modifier = Modifier.padding(top = 24.dp)) {
            Text(
                text = context.getString(R.string.recent_transactions),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // 显示最近的交易（按日期排序，取最新的10条）
            val recentTransactions = allTransactions.transactions
                .sortedByDescending { 
                    when (it) {
                        is Transaction.Income -> it.date
                        is Transaction.Expense -> it.date
                    }
                }
                .take(10)
            
            if (recentTransactions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(recentTransactions) { transaction ->
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
                Text(
                    text = context.getString(R.string.no_transactions),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
