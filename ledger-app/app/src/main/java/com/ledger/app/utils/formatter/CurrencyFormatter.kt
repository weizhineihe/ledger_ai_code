/**
 * 货币格式化工具
 * 提供货币的格式化和解析功能
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.utils.formatter

import com.ledger.app.utils.common.Constants

/**
 * 货币格式化工具
 * 用于格式化货币显示和解析货币字符串
 */
object CurrencyFormatter {
    /**
     * 格式化货币金额
     *
     * @param amount 金额
     * @param currency 货币类型
     * @return 格式化后的货币字符串
     */
    fun format(amount: Double, currency: String = Constants.DEFAULT_CURRENCY): String {
        val symbol = getCurrencySymbol(currency)
        return when (currency) {
            Constants.DEFAULT_CURRENCY -> "$symbol${String.format("%.2f", amount)}"
            else -> "$symbol${String.format("%.2f", amount)}"
        }
    }

    /**
     * 获取货币符号
     *
     * @param currency 货币类型
     * @return 货币符号
     */
    fun getCurrencySymbol(currency: String): String {
        return when (currency) {
            "CNY" -> Constants.CURRENCY_SYMBOL_CNY
            "USD" -> Constants.CURRENCY_SYMBOL_USD
            "EUR" -> Constants.CURRENCY_SYMBOL_EUR
            else -> Constants.CURRENCY_SYMBOL_CNY
        }
    }

    /**
     * 解析货币字符串为数字
     *
     * @param amountString 货币字符串
     * @return 解析后的金额
     */
    fun parse(amountString: String): Double {
        val cleanedString = amountString
            .replace("[¥$€]".toRegex(), "")
            .replace(",", "")
            .trim()
        return cleanedString.toDoubleOrNull() ?: 0.0
    }
}
