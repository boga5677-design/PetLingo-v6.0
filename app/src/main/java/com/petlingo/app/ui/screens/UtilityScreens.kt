package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.Word

@Composable
fun FavoritesScreen(words: List<Word>, favorites: Set<Int>, onToggle: (Int) -> Unit) {
    val favoriteWords = words.filter { it.id in favorites }
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("我的收藏", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold) }
        item { Text("收藏的單字可用於專屬測驗。") }
        if (favoriteWords.isEmpty()) item { Text("目前尚未收藏單字。") }
        items(favoriteWords) { word ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(14.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(word.english, fontWeight = FontWeight.Bold)
                        Text(word.chinese)
                    }
                    IconButton(onClick = { onToggle(word.id) }) { Icon(Icons.Default.Favorite, "取消收藏") }
                }
            }
        }
    }
}

@Composable
fun DailyMissionScreen(answered: Int, onStartQuiz: () -> Unit) {
    val progress = answered.coerceIn(0, 20)
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("每日任務", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("成人版每天完成 20 題即可獲得獎勵。")
        LinearProgressIndicator(progress = { progress / 20f }, modifier = Modifier.fillMaxWidth())
        Text("$progress / 20 題", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Button(onClick = onStartQuiz, modifier = Modifier.fillMaxWidth()) { Text("開始 20 題測驗") }
    }
}

@Composable
fun AchievementsScreen(sessionCount: Int, answered: Int) {
    val achievements = listOf(
        Triple("初次挑戰", "完成第一份測驗", sessionCount >= 1),
        Triple("持續進步", "完成 5 份測驗", sessionCount >= 5),
        Triple("今日任務", "一天完成 20 題", answered >= 20),
        Triple("測驗達人", "完成 20 份測驗", sessionCount >= 20)
    )
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("成就與獎勵", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold) }
        items(achievements) { (title, description, unlocked) ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (unlocked) Icons.Default.EmojiEvents else Icons.Default.Lock, null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(title, fontWeight = FontWeight.Bold)
                        Text(description)
                        Text(if (unlocked) "已解鎖" else "尚未解鎖", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("設定", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("正式版套件名稱", fontWeight = FontWeight.Bold)
                Text("com.petlingo.learning")
                Text("兒童版建議使用 com.petlingo.kids，可同時安裝而不互相覆蓋。", style = MaterialTheme.typography.bodySmall)
            }
        }
        Text("更多音效、顯示模式與提醒設定將在後續版本逐步加入。")
    }
}
