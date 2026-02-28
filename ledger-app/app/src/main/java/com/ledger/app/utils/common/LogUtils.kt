package com.ledger.app.utils.common

import android.util.Log
import com.ledger.app.BuildConfig

/**
 * Log工具类
 * 
 * 提供日志输出功能，支持debug包输出log，release包不输出
 */
object LogUtils {
    
    /**
     * 日志标签前缀
     */
    private const val TAG_PREFIX = "LedgerApp_"
    
    /**
     * 是否是debug模式
     */
    private val isDebug = BuildConfig.DEBUG
    
    /**
     * 输出verbose级别日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     */
    fun v(tag: String, message: String) {
        if (isDebug) {
            Log.v(TAG_PREFIX + tag, message)
        }
    }
    
    /**
     * 输出debug级别日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     */
    fun d(tag: String, message: String) {
        if (isDebug) {
            Log.d(TAG_PREFIX + tag, message)
        }
    }
    
    /**
     * 输出info级别日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     */
    fun i(tag: String, message: String) {
        if (isDebug) {
            Log.i(TAG_PREFIX + tag, message)
        }
    }
    
    /**
     * 输出warn级别日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     */
    fun w(tag: String, message: String) {
        if (isDebug) {
            Log.w(TAG_PREFIX + tag, message)
        }
    }
    
    /**
     * 输出error级别日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     */
    fun e(tag: String, message: String) {
        if (isDebug) {
            Log.e(TAG_PREFIX + tag, message)
        }
    }
    
    /**
     * 输出error级别日志
     * 
     * @param tag 日志标签
     * @param message 日志消息
     * @param throwable 异常
     */
    fun e(tag: String, message: String, throwable: Throwable) {
        if (isDebug) {
            Log.e(TAG_PREFIX + tag, message, throwable)
        }
    }
}
