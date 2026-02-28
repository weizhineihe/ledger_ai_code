/**
 * 应用类
 * 应用的入口点，配置Hilt依赖注入
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用类
 * 使用Hilt进行依赖注入的应用入口点
 */
@HiltAndroidApp
class App : Application()
