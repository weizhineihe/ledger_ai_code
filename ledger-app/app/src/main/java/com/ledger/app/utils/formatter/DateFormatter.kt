/**
 * 日期格式化工具
 * 提供日期的格式化和解析功能
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.utils.formatter

import com.ledger.app.utils.common.Constants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * 日期格式化工具
 * 用于格式化日期显示和解析日期字符串
 */
object DateFormatter {
    /**
     * 格式化日期
     *
     * @param date 日期时间对象
     * @return 格式化后的日期字符串
     */
    fun formatDate(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))
    }

    /**
     * 格式化日期
     *
     * @param date 日期对象
     * @return 格式化后的日期字符串
     */
    fun formatDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))
    }

    /**
     * 格式化日期时间
     *
     * @param date 日期时间对象
     * @return 格式化后的日期时间字符串
     */
    fun formatDateTime(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT))
    }

    /**
     * 格式化月年
     *
     * @param date 日期时间对象
     * @return 格式化后的月年字符串
     */
    fun formatMonthYear(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(Constants.MONTH_YEAR_FORMAT))
    }

    /**
     * 格式化年份
     *
     * @param date 日期时间对象
     * @return 格式化后的年份字符串
     */
    fun formatYear(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofPattern(Constants.YEAR_FORMAT))
    }

    /**
     * 短格式格式化日期
     *
     * @param date 日期时间对象
     * @return 短格式日期字符串
     */
    fun formatDateShort(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
    }

    /**
     * 短格式格式化日期时间
     *
     * @param date 日期时间对象
     * @return 短格式日期时间字符串
     */
    fun formatDateTimeShort(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
    }

    /**
     * 解析日期字符串
     *
     * @param dateString 日期字符串
     * @return 解析后的日期时间对象，解析失败返回null
     */
    fun parseDate(dateString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(Constants.DATE_FORMAT))
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 解析日期时间字符串
     *
     * @param dateTimeString 日期时间字符串
     * @return 解析后的日期时间对象，解析失败返回null
     */
    fun parseDateTime(dateTimeString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT))
        } catch (e: Exception) {
            null
        }
    }
}
