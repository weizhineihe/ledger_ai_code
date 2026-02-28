/**
 * 添加/编辑交易屏幕
 * 用于添加新交易或编辑现有交易
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.1
 */
package com.ledger.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import com.ledger.app.data.entity.CategoryEntity
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.data.model.TransactionInput
import com.ledger.app.utils.formatter.DateFormatter
import com.ledger.app.viewmodel.category.CategoryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.compose.material3.ExperimentalMaterial3Api

/**
 * 添加/编辑交易屏幕
 *
 * @param transactionId 交易ID，null表示添加新交易，非null表示编辑现有交易
 * @param onSave 保存按钮点击回调，接收交易输入数据
 * @param onCancel 取消按钮点击回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    transactionId: Long? = null,
    onSave: (TransactionInput) -> Unit,
    onCancel: () -> Unit,
    categoryViewModel: CategoryViewModel
) {
    val context = LocalContext.current
    var amount by remember { mutableStateOf("") } // 交易金额
    var amountError by remember { mutableStateOf(false) } // 金额输入错误状态
    var amountErrorMessage by remember { mutableStateOf("") } // 金额输入错误信息
    var type by remember { mutableStateOf(TransactionType.EXPENSE) } // 交易类型
    var categoryId by remember { mutableStateOf(0L) } // 分类ID
    var selectedCategoryName by remember { mutableStateOf("") } // 选中的分类名称
    var date by remember { mutableStateOf(LocalDate.now()) } // 交易日期，默认值为当前日期
    var description by remember { mutableStateOf("") } // 交易描述
    var showDatePicker by remember { mutableStateOf(false) } // 日期选择器显示状态
    var showCategoryDropdown by remember { mutableStateOf(false) } // 分类下拉菜单显示状态
    var categories by remember { mutableStateOf(listOf<CategoryEntity>()) } // 分类列表
    var categoryError by remember { mutableStateOf(false) } // 分类选择错误状态
    var categoryErrorMessage by remember { mutableStateOf("") } // 分类选择错误信息

    // 加载分类数据
    androidx.compose.runtime.LaunchedEffect(key1 = type) {
        // 初始化默认分类（如果需要）
        categoryViewModel.initializeDefaultCategories()
        
        // 模拟分类数据，实际项目中应该从ViewModel加载
        // 由于我们需要确保有分类可用，这里提供一些默认分类
        val defaultCategories = when (type) {
            TransactionType.INCOME -> listOf(
                CategoryEntity(id = 1, name = context.getString(R.string.category_salary), type = TransactionType.INCOME, icon = "account_balance_wallet", color = "#4CAF50", isDefault = true),
                CategoryEntity(id = 2, name = context.getString(R.string.category_bonus), type = TransactionType.INCOME, icon = "card_giftcard", color = "#2196F3", isDefault = true),
                CategoryEntity(id = 3, name = context.getString(R.string.category_investment), type = TransactionType.INCOME, icon = "trending_up", color = "#FF9800", isDefault = true),
                CategoryEntity(id = 4, name = context.getString(R.string.category_other_income), type = TransactionType.INCOME, icon = "attach_money", color = "#9C27B0", isDefault = true)
            )
            TransactionType.EXPENSE -> listOf(
                CategoryEntity(id = 1, name = context.getString(R.string.category_food), type = TransactionType.EXPENSE, icon = "restaurant", color = "#F44336", isDefault = true),
                CategoryEntity(id = 2, name = context.getString(R.string.category_transportation), type = TransactionType.EXPENSE, icon = "directions_car", color = "#2196F3", isDefault = true),
                CategoryEntity(id = 3, name = context.getString(R.string.category_shopping), type = TransactionType.EXPENSE, icon = "shopping_cart", color = "#9C27B0", isDefault = true),
                CategoryEntity(id = 4, name = context.getString(R.string.category_entertainment), type = TransactionType.EXPENSE, icon = "movie", color = "#FF9800", isDefault = true),
                CategoryEntity(id = 5, name = context.getString(R.string.category_housing), type = TransactionType.EXPENSE, icon = "home", color = "#4CAF50", isDefault = true),
                CategoryEntity(id = 6, name = context.getString(R.string.category_utilities), type = TransactionType.EXPENSE, icon = "flash_on", color = "#00BCD4", isDefault = true),
                CategoryEntity(id = 7, name = context.getString(R.string.category_healthcare), type = TransactionType.EXPENSE, icon = "local_hospital", color = "#E91E63", isDefault = true),
                CategoryEntity(id = 8, name = context.getString(R.string.category_other_expense), type = TransactionType.EXPENSE, icon = "more_horiz", color = "#607D8B", isDefault = true)
            )
        }
        
        categories = defaultCategories
        // 默认选择第一个分类
        if (categories.isNotEmpty()) {
            categoryId = categories[0].id
            selectedCategoryName = categories[0].name
            categoryError = false
            categoryErrorMessage = ""
        }
    }

    /**
     * 验证金额输入是否有效
     * @param input 输入的金额字符串
     * @return 是否是有效的金额格式
     */
    fun validateAmount(input: String): Boolean {
        if (input.isEmpty()) {
            amountError = false
            amountErrorMessage = ""
            return true
        }
        
        // 正则表达式：匹配数字，可选小数点和最多两位小数
        val amountRegex = "^\\d+(\\.\\d{1,2})?$".toRegex()
        
        if (!amountRegex.matches(input)) {
            amountError = true
            amountErrorMessage = "请输入有效的金额（最多两位小数）"
            return false
        }
        
        // 检查金额是否为正数
        val amountValue = input.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            amountError = true
            amountErrorMessage = "金额必须大于0"
            return false
        }
        
        amountError = false
        amountErrorMessage = ""
        return true
    }

    /**
     * 验证分类选择是否有效
     * @return 是否选择了有效的分类
     */
    fun validateCategory(): Boolean {
        if (categoryId <= 0 || selectedCategoryName.isEmpty()) {
            categoryError = true
            categoryErrorMessage = "请选择一个分类"
            return false
        }
        
        categoryError = false
        categoryErrorMessage = ""
        return true
    }

    val isEditMode = transactionId != null
    val screenTitle = if (isEditMode) context.getString(R.string.edit_transaction) else context.getString(R.string.add_transaction)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = screenTitle,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // TODO: Implement form fields
        // 1. Amount field
        // 2. Transaction type selector
        // 3. Category selector
        // 4. Date picker
        // 5. Description field

        // Placeholder form fields
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    validateAmount(it)
                },
                label = { Text(context.getString(R.string.amount)) },
                modifier = Modifier.fillMaxWidth(),
                isError = amountError
            )
            if (amountError) {
                Text(
                    text = amountErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Transaction type selector
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(context.getString(R.string.type) + ":")
            Row {
                Button(
                    onClick = { type = TransactionType.INCOME },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == TransactionType.INCOME) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                ) {
                    Text(context.getString(R.string.income))
                }
                Button(
                    onClick = { type = TransactionType.EXPENSE },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (type == TransactionType.EXPENSE) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                ) {
                    Text(context.getString(R.string.expense))
                }
            }
        }

        // Category selector
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(context.getString(R.string.category) + ":")
            OutlinedTextField(
                value = selectedCategoryName,
                onValueChange = { },
                label = { Text(context.getString(R.string.category)) },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Button(onClick = { showCategoryDropdown = true }) {
                        Text(context.getString(R.string.select))
                    }
                },
                isError = categoryError
            )
            if (categoryError) {
                Text(
                    text = categoryErrorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // Category dropdown menu
            DropdownMenu(
                expanded = showCategoryDropdown,
                onDismissRequest = { showCategoryDropdown = false },
                modifier = Modifier.width(240.dp)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            categoryId = category.id
                            selectedCategoryName = category.name
                            categoryError = false
                            categoryErrorMessage = ""
                            showCategoryDropdown = false
                        }
                    )
                }
            }
        }

        // Date picker
        OutlinedTextField(
            value = DateFormatter.formatDate(date),
            onValueChange = { /* 禁用直接输入，通过日期选择器修改 */ },
            label = { Text(context.getString(R.string.date)) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            readOnly = true,
            trailingIcon = {
                Button(onClick = { showDatePicker = true }) {
                    Text(context.getString(R.string.select))
                }
            }
        )

        // Date picker dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.toEpochDay() * 86400000)
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            date = LocalDate.ofEpochDay(it / 86400000)
                        }
                        showDatePicker = false
                    }) {
                        Text(context.getString(R.string.ok))
                    }
                },
                dismissButton = {
                    Button(onClick = { showDatePicker = false }) {
                        Text(context.getString(R.string.cancel))
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Description field
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(context.getString(R.string.description)) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        )

        // Action buttons
        Row(modifier = Modifier.padding(top = 32.dp)) {
            Button(onClick = onCancel, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(context.getString(R.string.cancel))
            }
            Button(
                onClick = {
                    // 保存前验证金额输入和分类选择
                    if (validateAmount(amount) && amount.isNotEmpty() && validateCategory()) {
                        // 创建TransactionInput对象
                        val transactionInput = TransactionInput(
                            amount = amount.toDouble(),
                            type = type,
                            categoryId = categoryId,
                            date = date.atStartOfDay(), // 将LocalDate转换为LocalDateTime
                            description = if (description.isEmpty()) null else description
                        )
                        onSave(transactionInput)
                    }
                },
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            ) {
                Text(context.getString(R.string.save))
            }
        }
    }
}
