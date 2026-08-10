package com.petlingo.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petlingo.app.R
import com.petlingo.app.model.QuizSession
import com.petlingo.app.util.AnalyticsCalculator

@Composable
fun ResultScreen(session: QuizSession?, onAnalytics: () -> Unit, onHome: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "本次測驗完成",
                    Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
            }

            item {
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(com.petlingo.app.R.drawable.petlingo_hero),
                            contentDescription = "可愛寵物替你加油",
                            modifier = Modifier.fillMaxWidth().heightIn(max = 170.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            encouragement(session?.score ?: 0),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (session == null) {
                item { Text("尚無測驗資料") }
            } else {
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ScoreBlock("${session.correctCount}/${session.questionCount}", "答對題數")
                        ScoreBlock("${session.score} 分", "本次分數")
                    }
                }

                item {
                    Card {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            ResultLine("總作答時間", AnalyticsCalculator.totalTime(session.totalMillis))
                            ResultLine(
                                "平均每題時間",
                                AnalyticsCalculator.seconds(AnalyticsCalculator.averageMillis(session.answers))
                            )
                            ResultLine("修改答案", "${AnalyticsCalculator.changedCount(session)} 題")
                        }
                    }
                }

                itemsIndexed(session.answers) { index, answer ->
                    Text(
                        "Q${index + 1} ${if (answer.isCorrect) "✓" else "✗"} " +
                            AnalyticsCalculator.seconds(answer.elapsedMillis)
                    )
                }
            }
        }

        Surface(shadowElevation = 8.dp) {
            Column(
                Modifier.fillMaxWidth().padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
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
private fun ScoreBlock(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

private fun encouragement(score: Int): String = when {
    score >= 90 -> "太厲害了！黑糖、偶貴、熊熊都替你開心！ 🎉"
    score >= 70 -> "表現很不錯，再複習一下就更強了！ 🐾"
    score >= 50 -> "有進步就是好事，一起把錯題再練一次！ 💪"
    else -> "黑糖、偶貴、熊熊陪你慢慢練習！ 🌟"
}

@Composable
private fun ResultLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text(value, fontWeight = FontWeight.Bold)
    }
}
