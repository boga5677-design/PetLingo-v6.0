package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.QuizMode

@Composable
fun ListeningSetupScreen(
    onStart: (Int, QuizMode) -> Boolean,
    onReady: () -> Unit
) {
    var count by remember { mutableIntStateOf(20) }
    var warning by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(Icons.Default.Headphones, null,
            modifier = Modifier.size(78.dp),
            tint = MaterialTheme.colorScheme.primary)
        Text("聽力測驗",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)
        Text(
            "播放英文單字後，直接選出正確中文意思。每題可重播。",
            textAlign = TextAlign.Center
        )

        Text("題數", fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth())

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(10, 20, 40).forEach { option ->
                FilterChip(
                    selected = count == option,
                    onClick = { count = option },
                    label = { Text("$option 題") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Card(colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text("測驗方式", fontWeight = FontWeight.Bold)
                Text("• 題目不顯示英文文字")
                Text("• 點擊喇叭播放或重播")
                Text("• 從四個中文選項中作答")
                Text("• 記錄每題反應時間")
            }
        }

        warning?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                if (onStart(count, QuizMode.LISTENING)) onReady()
                else warning = "題庫不足，無法建立聽力測驗。"
            },
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
        ) {
            Text("開始聽力測驗")
        }
    }
}
