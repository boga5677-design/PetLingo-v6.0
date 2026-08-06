package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.data.AppSettings
import com.petlingo.app.model.Word

@Composable
fun FavoritesScreen(
    words: List<Word>,
    favorites: Set<Int>,
    onToggle: (Int) -> Unit
) {
    val favoriteWords = words.filter { it.id in favorites }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "我的收藏",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "收藏的單字可用於專屬測驗。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (favoriteWords.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        text = "目前尚未收藏單字。",
                        modifier = Modifier.padding(18.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(
            items = favoriteWords,
            key = { it.id }
        ) { word ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = word.english,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = word.chinese,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (word.level.isNotBlank()) {
                            Text(
                                text = word.level,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            onToggle(word.id)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "取消收藏",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyMissionScreen(
    answered: Int,
    onStartQuiz: () -> Unit
) {
    val progress = answered.coerceIn(0, 20)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "每日任務",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "成人版每天完成 20 題即可獲得獎勵。",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "$progress / 20 題",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                LinearProgressIndicator(
                    progress = {
                        progress / 20f
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = when {
                        progress >= 20 ->
                            "今日任務已完成！"

                        progress == 0 ->
                            "今天尚未開始作答。"

                        else ->
                            "再完成 ${20 - progress} 題即可完成今日任務。"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Button(
            onClick = onStartQuiz,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 54.dp)
        ) {
            Text(
                text = if (progress >= 20) {
                    "再挑戰一次"
                } else {
                    "開始 20 題測驗"
                }
            )
        }
    }
}

@Composable
fun AchievementsScreen(
    sessionCount: Int,
    answered: Int
) {
    val achievements = listOf(
        AchievementItem(
            title = "初次挑戰",
            description = "完成第一份測驗",
            unlocked = sessionCount >= 1
        ),
        AchievementItem(
            title = "持續進步",
            description = "完成 5 份測驗",
            unlocked = sessionCount >= 5
        ),
        AchievementItem(
            title = "今日任務",
            description = "一天完成 20 題",
            unlocked = answered >= 20
        ),
        AchievementItem(
            title = "測驗達人",
            description = "完成 20 份測驗",
            unlocked = sessionCount >= 20
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "成就與獎勵",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "完成學習任務即可解鎖徽章。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(achievements) { achievement ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (achievement.unlocked) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (achievement.unlocked) {
                            Icons.Default.EmojiEvents
                        } else {
                            Icons.Default.Lock
                        },
                        contentDescription = null,
                        tint = if (achievement.unlocked) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Column {
                        Text(
                            text = achievement.title,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = achievement.description,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = if (achievement.unlocked) {
                                "已解鎖"
                            } else {
                                "尚未解鎖"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (achievement.unlocked) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdate: (AppSettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 18.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "設定",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "調整發音、測驗、顯示與學習偏好。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SettingsSection(
                title = "發音與聲音",
                icon = Icons.Default.VolumeUp
            ) {
                Text(
                    text = "預設發音",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf(
                        "美式",
                        "英式"
                    ),
                    selected = settings.accent,
                    onSelected = { value ->
                        onUpdate(
                            settings.copy(
                                accent = value
                            )
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "語音速度：${
                        String.format(
                            "%.2f",
                            settings.speechRate
                        )
                    }×"
                )

                Slider(
                    value = settings.speechRate,
                    onValueChange = { value ->
                        onUpdate(
                            settings.copy(
                                speechRate = value
                            )
                        )
                    },
                    valueRange = 0.70f..1.30f,
                    steps = 5
                )

                SettingSwitch(
                    title = "按鈕與答題音效",
                    description = "播放介面操作、答對與答錯提示音。",
                    checked = settings.soundEffects,
                    onCheckedChange = { checked ->
                        onUpdate(
                            settings.copy(
                                soundEffects = checked
                            )
                        )
                    }
                )

                SettingSwitch(
                    title = "題目自動朗讀",
                    description = "進入單字或聽力題目時，自動播放英文。",
                    checked = settings.autoReadQuestion,
                    onCheckedChange = { checked ->
                        onUpdate(
                            settings.copy(
                                autoReadQuestion = checked
                            )
                        )
                    }
                )
            }
        }

        item {
            SettingsSection(
                title = "測驗與學習",
                icon = Icons.Default.Quiz
            ) {
                Text(
                    text = "預設題數",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf(
                        "10",
                        "20",
                        "40"
                    ),
                    selected =
                        settings.defaultQuestionCount.toString(),
                    onSelected = { value ->
                        onUpdate(
                            settings.copy(
                                defaultQuestionCount =
                                    value.toInt()
                            )
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "預設單字級數",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf(
                        "全部",
                        "初級",
                        "中級",
                        "中高級"
                    ),
                    selected = settings.defaultLevel,
                    onSelected = { value ->
                        onUpdate(
                            settings.copy(
                                defaultLevel = value
                            )
                        )
                    }
                )

                SettingSwitch(
                    title = "答題後顯示解析",
                    description = "顯示正確答案、中文意思與題目說明。",
                    checked = settings.showExplanation,
                    onCheckedChange = { checked ->
                        onUpdate(
                            settings.copy(
                                showExplanation = checked
                            )
                        )
                    }
                )

                SettingSwitch(
                    title = "錯題自動加入錯題本",
                    description = "答錯後自動保存，方便之後複習。",
                    checked =
                        settings.addWrongAnswerAutomatically,
                    onCheckedChange = { checked ->
                        onUpdate(
                            settings.copy(
                                addWrongAnswerAutomatically =
                                    checked
                            )
                        )
                    }
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "每日任務目標",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf(
                        "10",
                        "20",
                        "40"
                    ),
                    selected =
                        settings.dailyGoal.toString(),
                    onSelected = { value ->
                        onUpdate(
                            settings.copy(
                                dailyGoal =
                                    value.toInt()
                            )
                        )
                    }
                )
            }
        }

        item {
            SettingsSection(
                title = "顯示與提醒",
                icon = Icons.Default.Palette
            ) {
                Text(
                    text = "顯示模式",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf(
                        "系統",
                        "淺色",
                        "深色"
                    ),
                    selected = settings.themeMode,
                    onSelected = { value ->
                        onUpdate(
                            settings.copy(
                                themeMode = value
                            )
                        )
                    }
                )

                SettingSwitch(
                    title = "大型文字",
                    description = "增加設定與學習頁面的文字可讀性。",
                    checked = settings.largeText,
                    onCheckedChange = { checked ->
                        onUpdate(
                            settings.copy(
                                largeText = checked
                            )
                        )
                    }
                )

                SettingSwitch(
                    title = "每日學習提醒",
                    description = "保存每日學習提醒偏好。",
                    checked = settings.dailyReminder,
                    onCheckedChange = { checked ->
                        onUpdate(
                            settings.copy(
                                dailyReminder = checked
                            )
                        )
                    }
                )
            }
        }

        item {
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {
        choices.forEach { value ->
            FilterChip(
                selected = selected == value,
                onClick = {
                    onSelected(value)
                },
                label = {
                    Text(
                        text = value,
                        maxLines = 1
                    )
                },
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = description,
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

private data class AchievementItem(
    val title: String,
    val description: String,
    val unlocked: Boolean
)
