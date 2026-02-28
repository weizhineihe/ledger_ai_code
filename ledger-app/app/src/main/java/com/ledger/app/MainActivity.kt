/**
 * 主活动类
 * 应用的主入口活动
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ledger.app.ui.navigation.AppNavigation
import com.ledger.app.ui.theme.LedgerAppTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * 主活动
 * 应用的主入口，设置Compose内容
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LedgerAppTheme {
                // 使用主题中的'background'颜色的表面容器
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
