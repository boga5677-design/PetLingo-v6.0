package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.QuizMode

@Composable
fun QuizSetupScreen(
    favoriteCount: Int,
    onStart: (Int, QuizMode) -> Boolean,
    onReady: () -> Unit
) {
    var count by remember { mutableIntStateOf(20) }
    var mode by remember { mutableStateOf(QuizMode.ENGLISH_TO_CHINESE) }
    var warning by remember { mutableStateOf<String?>(null) }

    val normalModes = listOf(
        QuizMode.ENGLISH_TO_CHINESE,
        QuizMode.CHINESE_TO_ENGLISH,
        QuizMode.FAVORITES
    )

    Column(
        Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("建立本次測驗", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("每次測驗獨立計分，不計算歷次平均分數。")

        Text("題型", fontWeight = FontWeight.Bold)
        normalModes.forEach { item ->
            val description = when (item) {
                QuizMode.ENGLISH_TO_CHINESE -> "英文題目，選擇正確中文。"
                QuizMode.CHINESE_TO_ENGLISH -> "中文題目，選擇正確英文。"
                QuizMode.FAVORITES -> "只使用收藏單字，目前共 $favoriteCount 個。"
                else -> ""
            }
            Card(
                onClick = { mode = item; warning = null },
                colors = CardDefaults.cardColors(
                    containerColor = if (mode == item)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.fillMaxWidth().padding(14.dp)) {
                    RadioButton(selected = mode == item, onClick = null)
                    Spacer(Modifier.width(8.dp))
                    Column(Modifier.weight(1f)) {
                        Text(item.label, fontWeight = FontWeight.Bold)
                        Text(description, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Text("題數", fontWeight = FontWeight.Bold)
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

        if (count == 20) {
            Text("完成 20 題可累積成人版每日任務進度。",
                style = MaterialTheme.typography.bodySmall)
        }

        warning?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        Button(
            onClick = {
                if (onStart(count, mode)) onReady()
                else warning = if (mode == QuizMode.FAVORITES)
                    "請先收藏至少 4 個單字。"
                else
                    "題庫不足，無法建立測驗。"
            },
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
        ) {
            Text("開始測驗")
        }
        Spacer(Modifier.height(16.dp))
    }
}
