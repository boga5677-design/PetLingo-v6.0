package com.petlingo.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petlingo.app.R
import com.petlingo.app.model.QuizSession
import com.petlingo.app.util.AnalyticsCalculator

@Composable
fun HomeScreen(
    last: QuizSession?,
    favoriteCount: Int,
    todayAnswered: Int,
    onQuiz: () -> Unit,
    onVocabulary: () -> Unit,
    onPhrase: () -> Unit,
    onMock: () -> Unit,
    onAnalytics: () -> Unit,
    onHistory: () -> Unit,
    onReading: () -> Unit,
    onWrongAnswers: () -> Unit,
    onSpeaking: () -> Unit,
    onListening: () -> Unit,
    onFavorites: () -> Unit,
    onDailyMission: () -> Unit,
    onAchievements: () -> Unit,
    onSettings: () -> Unit
) {
    val progress = todayAnswered.coerceIn(0, 20)

    LazyColumn(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onSettings) { Icon(Icons.Default.Menu, "選單") }
                Text("PetLingo", style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Black, color = Color(0xFF3B2518))
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text("Hi, 學習者 👋", fontWeight = FontWeight.Bold)
                    Text("持續學習的每一天都很棒！", style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = onSettings) { Icon(Icons.Default.Notifications, "通知") }
            }
        }

        item {
            Card(shape = RoundedCornerShape(24.dp), modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.petlingo_home_banner),
                    contentDescription = "黑糖、偶貴與熊熊一起學英文",
                    modifier = Modifier.fillMaxWidth().aspectRatio(1.78f),
                    contentScale = ContentScale.Crop
                )
            }
        }

        item {
            Card(
                onClick = onDailyMission,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0D5)),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MilitaryTech, null, tint = Color(0xFFD89224), modifier = Modifier.size(42.dp))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("今日任務（成人版）", fontWeight = FontWeight.Bold)
                            Text("完成 20 題可獲得獎勵！", style = MaterialTheme.typography.bodySmall)
                        }
                        Text("$progress / 20", style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    }
                    LinearProgressIndicator(progress = { progress / 20f }, modifier = Modifier.fillMaxWidth())
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                FeatureRow(
                    HomeFeature("GEPT 單字學習", "初級・中級・中高級", Icons.Default.MenuBook, Color(0xFFE8F2D9), onVocabulary),
                    HomeFeature("片語學習", "常用片語與例句", Icons.Default.Chat, Color(0xFFFFE5E0), onPhrase)
                )
                FeatureRow(
                    HomeFeature("閱讀測驗", "多種文章・即時解析", Icons.Default.AutoStories, Color(0xFFFFF0D5), onReading),
                    HomeFeature("多益模擬題", "20／50／100 題", Icons.Default.Assignment, Color(0xFFEDE5FA), onMock)
                )
                FeatureRow(
                    HomeFeature("聽力測驗", "播放英文・選出中文", Icons.Default.Headphones, Color(0xFFE1EDFA), onListening),
                    HomeFeature("口說練習", "隨機抽題・單字片語表", Icons.Default.Mic, Color(0xFFEDE5FA), onSpeaking)
                )
                FeatureRow(
                    HomeFeature("學習分析", "答題時間・弱點分析", Icons.Default.Analytics, Color(0xFFDDF3EE), onAnalytics),
                    HomeFeature("錯題本", "錯題複習・加強記憶", Icons.Default.EditNote, Color(0xFFFFE4DE), onWrongAnswers)
                )
                FeatureRow(
                    HomeFeature("成就與獎勵", "累積徽章・兌換獎勵", Icons.Default.EmojiEvents, Color(0xFFFFF0C8), onAchievements),
                    HomeFeature("測驗設定", "10／20／40 題", Icons.Default.Quiz, Color(0xFFE8F2D9), onQuiz)
                )
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ToolCard("我的收藏", "$favoriteCount 個", Icons.Default.Favorite, onFavorites, Modifier.weight(1f))
                ToolCard("每日任務", "挑戰 20 題", Icons.Default.Today, onDailyMission, Modifier.weight(1f))
                ToolCard("學習紀錄", "查看歷次紀錄", Icons.Default.History, onHistory, Modifier.weight(1f))
                ToolCard("設定", "音效・顯示", Icons.Default.Settings, onSettings, Modifier.weight(1f))
            }
        }

        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(Modifier.weight(1f), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("最近學習", fontWeight = FontWeight.Bold)
                        if (last == null) Text("尚未完成測驗")
                        else {
                            Text(last.modeLabel, style = MaterialTheme.typography.bodySmall)
                            Text("${last.score} 分・正確 ${last.correctCount} 題", fontWeight = FontWeight.Bold,
                                color = Color(0xFF3B8B56))
                            Text(AnalyticsCalculator.totalTime(last.totalMillis), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
                Card(Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E8)),
                    shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text("今日建議", fontWeight = FontWeight.Bold)
                        Text("建議複習", style = MaterialTheme.typography.bodySmall)
                        Text("cooperation", fontWeight = FontWeight.Black)
                        Button(onClick = onPhrase, modifier = Modifier.fillMaxWidth()) { Text("立即複習") }
                    }
                }
            }
        }

        item {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
                Row(Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("連續學習天數", fontWeight = FontWeight.Bold)
                        Text("7 天", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
                        Text("太棒了！繼續加油！", style = MaterialTheme.typography.bodySmall)
                    }
                    Text("🔥", style = MaterialTheme.typography.displaySmall)
                }
            }
        }

        item {
            TextButton(onClick = onHistory, modifier = Modifier.fillMaxWidth()) { Text("查看完整學習紀錄") }
        }
        item { Spacer(Modifier.height(10.dp)) }
    }
}

private data class HomeFeature(
    val title: String, val subtitle: String, val icon: ImageVector,
    val color: Color, val action: () -> Unit
)

@Composable
private fun FeatureRow(a: HomeFeature, b: HomeFeature) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        FeatureTile(a, Modifier.weight(1f))
        FeatureTile(b, Modifier.weight(1f))
    }
}

@Composable
private fun FeatureTile(feature: HomeFeature, modifier: Modifier) {
    Card(onClick = feature.action, modifier = modifier.heightIn(min = 132.dp),
        colors = CardDefaults.cardColors(containerColor = feature.color), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.fillMaxSize().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Icon(feature.icon, null, modifier = Modifier.size(42.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(feature.title, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(feature.subtitle, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ToolCard(title: String, subtitle: String, icon: ImageVector, action: () -> Unit, modifier: Modifier) {
    Card(onClick = action, modifier = modifier.heightIn(min = 92.dp), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(8.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center, maxLines = 1)
        }
    }
}
