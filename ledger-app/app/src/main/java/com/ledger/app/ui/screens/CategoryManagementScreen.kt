package com.ledger.app.ui.screens

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.app.R
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.ui.components.CategoryItem
import com.ledger.app.viewmodel.category.CategoryViewModel

@Composable
fun CategoryManagementScreen(
    onAddCategory: () -> Unit,
    categoryViewModel: CategoryViewModel
) {
    val context = LocalContext.current
    
    // 初始化默认分类（如果需要）
    LaunchedEffect(Unit) {
        categoryViewModel.initializeDefaultCategories()
    }
    
    // 加载分类数据
    val incomeCategories by categoryViewModel.getIncomeCategories().collectAsState(emptyList())
    val expenseCategories by categoryViewModel.getExpenseCategories().collectAsState(emptyList())
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Column {
            // 标题和管理分类按钮
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = context.getString(R.string.categories),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // 管理分类按钮
                androidx.compose.material3.Button(
                    onClick = onAddCategory
                ) {
                    Text(context.getString(R.string.manage_categories))
                }
            }

            // 收入分类列表
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = context.getString(R.string.income),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(incomeCategories) {
                        CategoryItem(
                            id = it.id,
                            name = it.name,
                            type = it.type,
                            icon = it.icon,
                            color = Color.parseColor(it.color),
                            isDefault = it.isDefault,
                            onClick = { /* TODO: Navigate to edit category */ }
                        )
                    }
                }
            }

            // 支出分类列表
            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(
                    text = context.getString(R.string.expense),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
                    items(expenseCategories) {
                        CategoryItem(
                            id = it.id,
                            name = it.name,
                            type = it.type,
                            icon = it.icon,
                            color = Color.parseColor(it.color),
                            isDefault = it.isDefault,
                            onClick = { /* TODO: Navigate to edit category */ }
                        )
                    }
                }
            }
        }
        
        // 已在顶部添加了管理分类按钮，移除浮动操作按钮
    }
}
