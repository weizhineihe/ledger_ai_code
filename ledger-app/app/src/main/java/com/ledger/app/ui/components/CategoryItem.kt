/**
 * 分类项组件
 * 显示分类的详细信息
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
import com.ledger.app.data.entity.TransactionType
import com.ledger.app.ui.theme.Green500
import com.ledger.app.ui.theme.Red500

/**
 * 分类项
 *
 * @param id 分类ID
 * @param name 分类名称
 * @param type 交易类型
 * @param icon 分类图标
 * @param color 分类颜色
 * @param isDefault 是否为默认分类
 * @param onClick 点击事件回调
 */
@Composable
fun CategoryItem(
    id: Long,
    name: String,
    type: TransactionType,
    icon: String,
    color: Int,
    isDefault: Boolean,
    onClick: () -> Unit
) {
    val typeColor = if (type == TransactionType.INCOME) Green500 else Red500
    val typeText = if (type == TransactionType.INCOME) "收入" else "支出"

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
                    .background(Color(color))
            ) {
                // TODO: 实现图标
                Text(
                    text = icon.take(1).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // 显示默认标记
                    if (isDefault) {
                        Text(
                            text = "默认",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Text(
                    text = typeText,
                    fontSize = 14.sp,
                    color = typeColor
                )
            }
        }
        // 箭头图标
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(circularShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            Text(
                text = ">",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
