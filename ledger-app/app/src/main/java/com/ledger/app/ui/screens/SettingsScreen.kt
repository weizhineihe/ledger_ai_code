package com.ledger.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledger.app.R

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = context.getString(R.string.settings),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        // TODO: Implement settings content
        // 1. Currency settings
        // 2. Theme settings
        // 3. Data management (export/import/backup)
        // 4. About section

        // Placeholder for settings options
        Column(modifier = Modifier.padding(top = 16.dp)) {
            Text(
                text = context.getString(R.string.currency),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            // TODO: Implement currency selector
        }

        Column(modifier = Modifier.padding(top = 24.dp)) {
            Text(
                text = context.getString(R.string.theme),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            // TODO: Implement theme selector
        }

        Column(modifier = Modifier.padding(top = 24.dp)) {
            Text(
                text = context.getString(R.string.data_management),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(onClick = { /* TODO: Implement export */ }) {
                Text(context.getString(R.string.export))
            }
            Button(onClick = { /* TODO: Implement import */ }) {
                Text(context.getString(R.string.Import))
            }
            Button(onClick = { /* TODO: Implement backup */ }) {
                Text(context.getString(R.string.backup))
            }
        }
    }
}
