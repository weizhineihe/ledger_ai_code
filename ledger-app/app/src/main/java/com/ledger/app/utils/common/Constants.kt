/**
 * 常量类
 * 定义应用中使用的各种常量
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.utils.common

/**
 * 常量对象
 * 包含应用中使用的各种常量定义
 */
object Constants {
    // 数据库
    /** 数据库名称 */
    const val DATABASE_NAME = "ledger_database"

    // 日期格式
    /** 日期格式 */
    const val DATE_FORMAT = "yyyy-MM-dd"
    /** 日期时间格式 */
    const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm"
    /** 月年格式 */
    const val MONTH_YEAR_FORMAT = "MMMM yyyy"
    /** 年份格式 */
    const val YEAR_FORMAT = "yyyy"

    // 货币
    /** 默认货币 */
    const val DEFAULT_CURRENCY = "CNY"
    /** 人民币符号 */
    const val CURRENCY_SYMBOL_CNY = "¥"
    /** 美元符号 */
    const val CURRENCY_SYMBOL_USD = "$"
    /** 欧元符号 */
    const val CURRENCY_SYMBOL_EUR = "€"

    // 图标
    /** 收入图标列表 */
    val INCOME_ICONS = listOf(
        "account_balance_wallet",
        "card_giftcard",
        "trending_up",
        "attach_money",
        "business",
        "local_atm",
        "payments",
        "receipt"
    )

    /** 支出图标列表 */
    val EXPENSE_ICONS = listOf(
        "restaurant",
        "directions_car",
        "shopping_cart",
        "movie",
        "home",
        "flash_on",
        "local_hospital",
        "more_horiz",
        "local_grocery_store",
        "local_cafe",
        "transportation",
        "hotel",
        "fitness_center",
        "school",
        "work"
    )

    // 颜色
    /** 分类颜色列表 */
    val CATEGORY_COLORS = listOf(
        "#4CAF50", // 绿色
        "#2196F3", // 蓝色
        "#FF9800", // 橙色
        "#9C27B0", // 紫色
        "#F44336", // 红色
        "#00BCD4", // 青色
        "#FFC107", // 琥珀色
        "#607D8B", // 蓝灰色
        "#795548", // 棕色
        "#E91E63", // 粉色
        "#3F51B5", // 靛蓝色
        "#009688"  // 蓝绿色
    )

    // 主题
    /** 浅色主题 */
    const val THEME_LIGHT = "light"
    /** 深色主题 */
    const val THEME_DARK = "dark"
    /** 系统主题 */
    const val THEME_SYSTEM = "system"

    // 导出
    /** 导出目录 */
    const val EXPORT_DIR = "LedgerApp/Export"
    /** 备份目录 */
    const val BACKUP_DIR = "LedgerApp/Backup"

    // 默认值
    /** 默认分页大小 */
    const val DEFAULT_PAGE_SIZE = 20
    /** 默认最近交易记录限制 */
    const val DEFAULT_RECENT_TRANSACTIONS_LIMIT = 10
    /** 默认顶部分类限制 */
    const val DEFAULT_TOP_CATEGORIES_LIMIT = 5
}
