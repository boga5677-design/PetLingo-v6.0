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
fun ResultScreen(
    session: QuizSession?,
    onAnalytics: () -> Unit,
    onHome: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "本次測驗完成",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.petlingo_hero),
                            contentDescription = "黑糖、偶貴與熊熊替你加油",
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp),
                            contentScale = ContentScale.Fit
                        )

                        Text(
                            encouragement(session?.score ?: 0),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (session == null) {
                item {
                    Text("尚無測驗資料")
                }
            } else {
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ScoreBlock(
                            "${session.correctCount}/${session.questionCount}",
                            "答對題數"
                        )
                        ScoreBlock(
                            "${session.score} 分",
                            "本次分數"
                        )
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ResultLine(
                                "總作答時間",
                                AnalyticsCalculator.totalTime(session.totalMillis)
                            )
                            ResultLine(
                                "平均每題時間",
                                AnalyticsCalculator.seconds(
                                    AnalyticsCalculator.averageMillis(session.answers)
                                )
                            )
                            ResultLine(
                                "修改答案",
                                "${AnalyticsCalculator.changedCount(session)} 題"
                            )
                        }
                    }
                }

                item {
                    Text(
                        "本次成績獨立保存，不與其他測驗計算平均。",
                        style = MaterialTheme.typography.bodySmall
                    )
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
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Home, null)
                    Spacer(Modifier.width(8.dp))
                    Text("返回主選單")
                }

                OutlinedButton(
                    onClick = onAnalytics,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Analytics, null)
                    Spacer(Modifier.width(8.dp))
                    Text("查看本次答題分析")
                }
            }
        }
    }
}

@Composable
private fun ScoreBlock(
    value: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun encouragement(score: Int): String {
    return when {
        score >= 90 -> "黑糖、偶貴、熊熊：太厲害了！今天表現超棒！ 🎉"
        score >= 70 -> "三隻毛孩幫你加油：再複習一下就更強了！ 🐾"
        score >= 50 -> "有進步就是好事！一起把錯題再練一次吧！ 💪"
        else -> "別急，黑糖、偶貴、熊熊陪你慢慢練習！ 🌟"
    }
}

@Composable
private fun ResultLine(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, fontWeight = FontWeight.Bold)
    }
}
