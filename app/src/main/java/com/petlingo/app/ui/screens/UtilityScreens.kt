package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun SettingsScreen(
    settings: com.petlingo.app.data.AppSettings,
    onUpdate: (com.petlingo.app.data.AppSettings) -> Unit,
    sessionCount: Int,
    favoriteCount: Int,
    wrongAnswerCount: Int,
    speakingCount: Int,
    onClearHistory: () -> Unit,
    onClearWrongAnswers: () -> Unit,
    onClearSpeaking: () -> Unit,
    onResetSettings: () -> Unit
) {
    var confirmAction by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("設定", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "所有設定與學習紀錄只保存在這台裝置，不需要登入。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SettingsSection("發音與聲音", Icons.Default.VolumeUp) {
                Text("預設發音", fontWeight = FontWeight.Bold)
                ChoiceRow(
                    choices = listOf("美式", "英式"),
                    selected = settings.accent
                ) { onUpdate(settings.copy(accent = it)) }

                Spacer(Modifier.height(8.dp))
                Text("語音速度：${String.format("%.2f", settings.speechRate)}×")
                Slider(
                    value = settings.speechRate,
                    onValueChange = { onUpdate(settings.copy(speechRate = it)) },
                    valueRange = 0.70f..1.30f,
                    steps = 5
                )

                SettingSwitch(
                    "按鈕與答題音效",
                    "保留介面操作與答題提示音。",
                    settings.soundEffects
                ) { onUpdate(settings.copy(soundEffects = it)) }

                SettingSwitch(
                    "題目自動朗讀",
                    "進入單字或聽力題目時自動播放英文。",
                    settings.autoReadQuestion
                ) { onUpdate(settings.copy(autoReadQuestion = it)) }
            }
        }

        item {
            SettingsSection("測驗與學習", Icons.Default.Quiz) {
                Text("預設題數", fontWeight = FontWeight.Bold)
                ChoiceRow(
                    choices = listOf("10", "20", "40"),
                    selected = settings.defaultQuestionCount.toString()
                ) { onUpdate(settings.copy(defaultQuestionCount = it.toInt())) }

                Spacer(Modifier.height(10.dp))
                Text("預設單字級數", fontWeight = FontWeight.Bold)
                ChoiceRow(
                    choices = listOf("全部", "初級", "中級", "中高級"),
                    selected = settings.defaultLevel
                ) { onUpdate(settings.copy(defaultLevel = it)) }

                SettingSwitch(
                    "答題後顯示解析",
                    "顯示正確答案、中文意思與題目說明。",
                    settings.showExplanation
                ) { onUpdate(settings.copy(showExplanation = it)) }

                SettingSwitch(
                    "錯題自動加入錯題本",
                    "答錯後自動保存，方便之後複習。",
                    settings.addWrongAnswerAutomatically
                ) { onUpdate(settings.copy(addWrongAnswerAutomatically = it)) }

                Spacer(Modifier.height(8.dp))
                Text("每日任務目標", fontWeight = FontWeight.Bold)
                ChoiceRow(
                    choices = listOf("10", "20", "40"),
                    selected = settings.dailyGoal.toString()
                ) { onUpdate(settings.copy(dailyGoal = it.toInt())) }
            }
        }

        item {
            SettingsSection("顯示與提醒", Icons.Default.Palette) {
                Text("顯示模式", fontWeight = FontWeight.Bold)
                ChoiceRow(
                    choices = listOf("系統", "淺色", "深色"),
                    selected = settings.themeMode
                ) { onUpdate(settings.copy(themeMode = it)) }

                SettingSwitch(
                    "大型文字",
                    "增加設定與學習頁面的文字可讀性。",
                    settings.largeText
                ) { onUpdate(settings.copy(largeText = it)) }

                SettingSwitch(
                    "每日學習提醒",
                    "本版先保存偏好；系統通知排程將在後續版本接上。",
                    settings.dailyReminder
                ) { onUpdate(settings.copy(dailyReminder = it)) }
            }
        }

        item {
            SettingsSection("本機資料", Icons.Default.Storage) {
                DataCountRow("測驗紀錄", sessionCount)
                DataCountRow("收藏單字", favoriteCount)
                DataCountRow("錯題", wrongAnswerCount)
                DataCountRow("口說紀錄", speakingCount)

                HorizontalDivider(Modifier.padding(vertical = 8.dp))

                OutlinedButton(
                    onClick = { confirmAction = "history" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.DeleteSweep, null)
                    Spacer(Modifier.width(8.dp))
                    Text("清除測驗與閱讀紀錄")
                }

                OutlinedButton(
                    onClick = { confirmAction = "wrong" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Delete, null)
                    Spacer(Modifier.width(8.dp))
                    Text("清除錯題本")
                }

                OutlinedButton(
                    onClick = { confirmAction = "speaking" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.RecordVoiceOver, null)
                    Spacer(Modifier.width(8.dp))
                    Text("清除口說紀錄")
                }
            }
        }

        item {
            SettingsSection("App 資訊", Icons.Default.Info) {
                Text("PetLingo 成人版", fontWeight = FontWeight.Bold)
                Text("版本 6.4.0-local-settings")
                Text("正式版套件名稱：com.petlingo.learning")
                Text(
                    "兒童版建議使用 com.petlingo.kids，可同時安裝而不互相覆蓋。",
                    style = MaterialTheme.typography.bodySmall
                )
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text("帳戶功能：未啟用")
                Text("雲端同步：未啟用")
                Text("所有資料均保存在本機。", style = MaterialTheme.typography.bodySmall)
                OutlinedButton(
                    onClick = { confirmAction = "settings" },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.RestartAlt, null)
                    Spacer(Modifier.width(8.dp))
                    Text("恢復預設設定")
                }
            }
        }
    }

    if (confirmAction != null) {
        AlertDialog(
            onDismissRequest = { confirmAction = null },
            title = { Text("確認操作") },
            text = {
                Text(
                    when (confirmAction) {
                        "history" -> "確定清除所有測驗與閱讀紀錄？"
                        "wrong" -> "確定清除全部錯題？"
                        "speaking" -> "確定清除全部口說紀錄？"
                        else -> "確定將所有設定恢復預設值？"
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    when (confirmAction) {
                        "history" -> onClearHistory()
                        "wrong" -> onClearWrongAnswers()
                        "speaking" -> onClearSpeaking()
                        else -> onResetSettings()
                    }
                    confirmAction = null
                }) { Text("確定") }
            },
            dismissButton = {
                TextButton(onClick = { confirmAction = null }) { Text("取消") }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(Modifier.fillMaxWidth()) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider()
            content()
        }
    }
}

@Composable
private fun ChoiceRow(
    choices: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        choices.forEach { value ->
            FilterChip(
                selected = selected == value,
                onClick = { onSelected(value) },
                label = { Text(value) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun DataCountRow(label: String, count: Int) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text("$count 筆", fontWeight = FontWeight.Bold)
    }
}
