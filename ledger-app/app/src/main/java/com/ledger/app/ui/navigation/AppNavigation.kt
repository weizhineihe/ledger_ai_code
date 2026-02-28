/**
 * 应用导航
 * 管理应用的导航结构和路由
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.1
 */
package com.ledger.app.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.LaunchedEffect
import com.ledger.app.R
import com.ledger.app.data.model.TransactionInput
import com.ledger.app.ui.components.AddTransactionFAB
import com.ledger.app.ui.components.BottomNavigationBar
import com.ledger.app.ui.screens.AddEditTransactionScreen
import com.ledger.app.ui.screens.CategoryManagementScreen
import com.ledger.app.ui.screens.DashboardScreen
import com.ledger.app.ui.screens.TransactionListScreen
import com.ledger.app.ui.screens.SettingsScreen
import com.ledger.app.viewmodel.transaction.TransactionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ledger.app.viewmodel.category.CategoryViewModel

/**
 * 应用导航
 * 配置应用的导航结构和屏幕路由
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val transactionViewModel = viewModel<TransactionViewModel>()
    val categoryViewModel = viewModel<CategoryViewModel>()
    
    // 初始化默认分类
    LaunchedEffect(Unit) {
        categoryViewModel.initializeDefaultCategories()
    }
    
    // 获取当前路由
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val addEditTransactionRoute = context.getString(Screen.AddEditTransaction.route)
    val categoriesRoute = context.getString(Screen.Categories.route)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            // 只有当前不在添加/编辑交易屏幕和分类管理屏幕时才显示浮动操作按钮
            if (currentRoute != addEditTransactionRoute && currentRoute != categoriesRoute) {
                AddTransactionFAB(navController)
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Center,
        content = {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                NavHost(navController = navController, startDestination = context.getString(Screen.Dashboard.route)) {
                    // 仪表盘屏幕
                    composable(context.getString(Screen.Dashboard.route)) {
                        DashboardScreen(transactionViewModel = transactionViewModel)
                    }
                    // 交易列表屏幕
                    composable(context.getString(Screen.Transactions.route)) {
                        TransactionListScreen(
                            onAddTransaction = {
                                navController.navigate(context.getString(Screen.AddEditTransaction.route))
                            },
                            transactionViewModel = transactionViewModel
                        )
                    }
                    // 添加/编辑交易屏幕
                    composable(context.getString(Screen.AddEditTransaction.route)) {
                        AddEditTransactionScreen(
                            onSave = { transactionInput ->
                                transactionViewModel.createTransaction(transactionInput)
                                navController.navigateUp()
                            },
                            onCancel = {
                                navController.navigateUp()
                            },
                            categoryViewModel = categoryViewModel
                        )
                    }
                    // 分类管理屏幕
                    composable(context.getString(Screen.Categories.route)) {
                        CategoryManagementScreen(
                            onAddCategory = {
                                // TODO: 导航到添加分类屏幕
                            },
                            categoryViewModel = categoryViewModel
                        )
                    }
                    // 设置屏幕
                    composable(context.getString(Screen.Settings.route)) {
                        SettingsScreen()
                    }
                }
            }
        }
    )
}

/**
 * 屏幕路由密封类
 * 定义应用中所有的屏幕路由
 */
sealed class Screen(val route: Int) {
    /** 仪表盘屏幕 */
    data object Dashboard : Screen(R.string.dashboard)
    /** 交易列表屏幕 */
    data object Transactions : Screen(R.string.transactions)
    /** 添加/编辑交易屏幕 */
    data object AddEditTransaction : Screen(R.string.add_edit_transaction)
    /** 分类管理屏幕 */
    data object Categories : Screen(R.string.categories)
    /** 设置屏幕 */
    data object Settings : Screen(R.string.settings)
}
