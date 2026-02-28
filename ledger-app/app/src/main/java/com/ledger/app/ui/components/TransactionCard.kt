/**
 * 交易卡片组件
 * 显示交易记录的详细信息
 *
 * @author Ledger App Team
 * @since 2024-01-01
 * @version 1.0
 */
package com.ledger.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.app.R
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.utils.formatter.CurrencyFormatter
import com.ledger.app.ui.theme.Green500
import com.ledger.app.ui.theme.Red500

/**
 * 交易卡片
 *
 * @param id 交易ID
 * @param amount 交易金额
 * @param type 交易类型
 * @param categoryName 分类名称
 * @param categoryIcon 分类图标
 * @param categoryColor 分类颜色
 * @param date 交易日期
 * @param description 交易描述
 * @param onClick 点击事件回调
 */
@Composable
fun TransactionCard(
    id: Long,
    amount: Double,
    type: TransactionType,
    categoryName: String,
    categoryIcon: String,
    categoryColor: Int,
    date: String,
    description: String? = null,
    onClick: () -> Unit
) {
    val amountColor = if (type == TransactionType.INCOME) Green500 else Red500
    val context = androidx.compose.ui.platform.LocalContext.current

    // 获取本地化的分类名称
    val localizedCategoryName = getLocalizedCategoryName(context, categoryName)

    // 生成分类图标文本：英文显示每个单词的首字母，中文显示第一个字
    val categoryIconText = generateCategoryIconText(localizedCategoryName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val circularShape = MaterialTheme.shapes.medium
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 分类图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(circularShape)
                    .background(Color(categoryColor))
            ) {
                // 显示分类图标文本
                Text(
                    text = categoryIconText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column {
                Text(
                    text = localizedCategoryName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // 显示交易描述
                if (description != null) {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                Text(
                    text = date,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        // 交易金额
        Text(
            text = CurrencyFormatter.format(amount),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = amountColor
        )
    }
}

// 获取本地化的分类名称
private fun getLocalizedCategoryName(context: android.content.Context, categoryName: String): String {
    return when (categoryName) {
        "Salary" -> context.getString(R.string.category_salary)
        "Bonus" -> context.getString(R.string.category_bonus)
        "Investment" -> context.getString(R.string.category_investment)
        "Other Income" -> context.getString(R.string.category_other_income)
        "Food" -> context.getString(R.string.category_food)
        "Transportation" -> context.getString(R.string.category_transportation)
        "Shopping" -> context.getString(R.string.category_shopping)
        "Entertainment" -> context.getString(R.string.category_entertainment)
        "Housing" -> context.getString(R.string.category_housing)
        "Utilities" -> context.getString(R.string.category_utilities)
        "Healthcare" -> context.getString(R.string.category_healthcare)
        "Other Expense" -> context.getString(R.string.category_other_expense)
        else -> categoryName
    }
}

// 生成分类图标文本
private fun generateCategoryIconText(categoryName: String): String {
    // 检查是否包含空格（英文）
    if (categoryName.contains(" ")) {
        // 英文：显示每个单词的首字母
        return categoryName.split(" ")
            .map { it.take(1).uppercase() }
            .joinToString("")
            .take(2) // 最多显示两个字母
    } else {
        // 中文：显示第一个字
        return categoryName.take(1)
    }
}
