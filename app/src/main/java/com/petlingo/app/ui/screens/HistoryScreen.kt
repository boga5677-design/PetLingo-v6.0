package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.QuizSession
import com.petlingo.app.model.ReadingSession
import com.petlingo.app.util.AnalyticsCalculator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    sessions: List<QuizSession>,
    readingSessions: List<ReadingSession>,
    onOpen: (QuizSession) -> Unit,
    onClear: () -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("歷次獨立紀錄", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("每次測驗與閱讀各自保存，不顯示歷次平均分數。")
        }

        if (sessions.isNotEmpty()) item { Text("單字測驗", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        items(sessions) { session ->
            Card(onClick = { onOpen(session) }) {
                Column(Modifier.padding(16.dp)) {
                    Text(formatDate(session.finishedAt), fontWeight = FontWeight.Bold)
                    Text(session.modeLabel)
                    Text("${session.correctCount}/${session.questionCount} 題・${session.score} 分")
                    Text("總時間：${AnalyticsCalculator.totalTime(session.totalMillis)}")
                }
            }
        }

        if (readingSessions.isNotEmpty()) item { Text("閱讀練習", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        items(readingSessions) { session ->
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text(formatDate(session.finishedAt), fontWeight = FontWeight.Bold)
                    Text("${session.category}・${session.passageTitle}")
                    Text("${session.correctCount}/${session.questionCount} 題・${session.score} 分")
                    Text("總時間：${AnalyticsCalculator.totalTime(session.totalMillis)}")
                }
            }
        }

        if (sessions.isNotEmpty() || readingSessions.isNotEmpty()) {
            item { OutlinedButton(onClear, Modifier.fillMaxWidth()) { Text("清除全部紀錄") } }
        }
    }
}

private fun formatDate(timestamp: Long): String =
    SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date(timestamp))
