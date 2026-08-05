package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.QuizSession
import com.petlingo.app.util.AnalyticsCalculator

@Composable
fun ResultScreen(session: QuizSession?, onAnalytics: () -> Unit, onHome: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("本次測驗完成", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        if (session == null) {
            Text("尚無測驗資料")
        } else {
            Text("${session.correctCount}/${session.questionCount} 題", style = MaterialTheme.typography.displaySmall)
            Text("本次分數：${session.score} 分", style = MaterialTheme.typography.headlineSmall)
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ResultLine("總作答時間", AnalyticsCalculator.totalTime(session.totalMillis))
                    ResultLine("平均每題時間", AnalyticsCalculator.seconds(AnalyticsCalculator.averageMillis(session.answers)))
                    ResultLine("修改答案", "${AnalyticsCalculator.changedCount(session)} 題")
                }
            }
            Text("本次成績獨立保存，不與其他測驗計算平均。")
            session.answers.forEachIndexed { index, answer ->
                Text("Q${index + 1} ${if (answer.isCorrect) "✓" else "✗"} ${AnalyticsCalculator.seconds(answer.elapsedMillis)}")
            }
        }
        Button(onAnalytics, Modifier.fillMaxWidth()) { Text("查看本次答題分析") }
        OutlinedButton(onHome, Modifier.fillMaxWidth()) { Text("回首頁") }
    }
}

@Composable
private fun ResultLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text(value, fontWeight = FontWeight.Bold)
    }
}
