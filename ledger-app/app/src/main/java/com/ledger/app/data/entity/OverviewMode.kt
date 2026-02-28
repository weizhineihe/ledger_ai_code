/**
 * 财务概览模式枚举
 * 
 * 用于表示财务概览的不同时间范围模式
 * 
 * 创建日期: 2026-02-26
 */
package com.ledger.app.data.entity

/**
 * 财务概览模式
 * 
 * ALL: 所有时间
 * YEARLY: 每年
 * MONTHLY: 每月
 */
enum class OverviewMode {
    ALL,    // 所有时间
    YEARLY, // 每年
    MONTHLY // 每月
}
