package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.QuizSession
import com.petlingo.app.util.AnalyticsCalculator

@Composable
fun ResultScreen(session: QuizSession?, onAnalytics: () -> Unit, onHome: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("本次測驗完成", style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold)
            }
            if (session == null) {
                item { Text("尚無測驗資料") }
            } else {
                item {
                    Text("${session.correctCount}/${session.questionCount} 題",
                        style = MaterialTheme.typography.displaySmall)
                    Text("本次分數：${session.score} 分",
                        style = MaterialTheme.typography.headlineSmall)
                }
                item {
                    Card {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            ResultLine("總作答時間", AnalyticsCalculator.totalTime(session.totalMillis))
                            ResultLine("平均每題時間",
                                AnalyticsCalculator.seconds(AnalyticsCalculator.averageMillis(session.answers)))
                            ResultLine("修改答案", "${AnalyticsCalculator.changedCount(session)} 題")
                        }
                    }
                }
                item { Text("本次成績獨立保存，不與其他測驗計算平均。") }
                itemsIndexed(session.answers) { index, answer ->
                    Text("Q${index + 1} ${if (answer.isCorrect) "✓" else "✗"} " +
                        AnalyticsCalculator.seconds(answer.elapsedMillis))
                }
            }
        }
        Surface(shadowElevation = 8.dp) {
            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onHome, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Home, null)
                    Spacer(Modifier.width(8.dp))
                    Text("返回主選單")
                }
                OutlinedButton(onClick = onAnalytics, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Analytics, null)
                    Spacer(Modifier.width(8.dp))
                    Text("查看本次答題分析")
                }
            }
        }
    }
}

@Composable
private fun ResultLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text(value, fontWeight = FontWeight.Bold)
    }
}
