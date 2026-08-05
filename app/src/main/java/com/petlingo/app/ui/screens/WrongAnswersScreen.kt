package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.WrongAnswer

@Composable
fun WrongAnswersScreen(items: List<WrongAnswer>, onRemove: (String) -> Unit, onClear: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("錯題本", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                if (items.isNotEmpty()) TextButton(onClick = onClear) { Text("全部清除") }
            }
        }
        if (items.isEmpty()) item { Text("目前沒有錯題。答錯後會自動加入這裡。") }
        items(items, key = { it.key }) { item ->
            Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(item.typeLabel, color = MaterialTheme.colorScheme.primary)
                Text(item.prompt, fontWeight = FontWeight.Bold)
                Text("你的答案：${item.selectedAnswer}")
                Text("正確答案：${item.correctAnswer}")
                if (item.explanation.isNotBlank()) Text("解析：${item.explanation}")
                Text("答錯 ${item.wrongCount} 次・耗時 ${"%.1f".format(item.elapsedMillis/1000.0)} 秒")
                TextButton(onClick = { onRemove(item.key) }) { Text("標記為已熟悉並移除") }
            } }
        }
    }
}
