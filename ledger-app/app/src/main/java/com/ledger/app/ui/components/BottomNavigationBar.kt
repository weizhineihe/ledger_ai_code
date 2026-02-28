/**
 * 底部导航栏组件
 * 提供应用的主要导航功能
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.2
 */
package com.ledger.app.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ledger.app.R
import com.ledger.app.ui.navigation.Screen

/**
 * 底部导航栏
 *
 * @param navController 导航控制器
 */
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    // 导航项列表
    val items = listOf(
        Screen.Dashboard,
        Screen.Transactions,
        Screen.Categories,
        Screen.Settings
    )

    BottomAppBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    // 根据屏幕类型显示不同的图标
                    when (screen) {
                        Screen.Dashboard -> Text("📊")
                        Screen.Transactions -> Text("💳")
                        Screen.Categories -> Text("📁")
                        Screen.Settings -> Text("⚙️")
                        else -> Text("📱")
                    }
                },
                label = { Text(context.getString(screen.route)) },
                selected = currentRoute == context.getString(screen.route),
                onClick = {
                    if (currentRoute != context.getString(screen.route)) {
                        navController.navigate(context.getString(screen.route)) {
                            // 弹出到导航图的起始目的地，避免在返回栈中积累大量目的地
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            // 避免在重新选择同一项目时创建多个相同目的地的副本
                            launchSingleTop = true
                            // 重新选择先前选择的项目时恢复状态
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

/**
 * 添加记录浮动操作按钮
 * 位于屏幕底部中央，悬浮在导航栏上方
 *
 * @param navController 导航控制器
 */
@Composable
fun AddTransactionFAB(navController: NavHostController) {
    val context = LocalContext.current
    val clicked = androidx.compose.runtime.remember {
        androidx.compose.runtime.mutableStateOf(false)
    }
    
    androidx.compose.material3.FloatingActionButton(
        onClick = {
            // 防止重复点击
            if (!clicked.value) {
                clicked.value = true
                navController.navigate(context.getString(Screen.AddEditTransaction.route)) {
                    // 导航完成后重置点击状态
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
                // 重置点击状态，确保用户可以再次点击
                clicked.value = false
            }
        },
        containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50),
        contentColor = androidx.compose.ui.graphics.Color.White
    ) {
        Text("+")
    }
}
