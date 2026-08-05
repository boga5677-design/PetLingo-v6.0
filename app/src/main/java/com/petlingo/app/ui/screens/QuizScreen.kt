package com.petlingo.app.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.AnswerRecord
import com.petlingo.app.model.QuestionType
import com.petlingo.app.model.QuizQuestion
import com.petlingo.app.util.AnalyticsCalculator
import java.util.Locale

@Composable
fun QuizScreen(
    question: QuizQuestion?,
    index: Int,
    total: Int,
    onSelect: (Int) -> Unit,
    onSubmit: (Int) -> AnswerRecord?,
    onNext: () -> Boolean,
    onFinished: () -> Unit
) {
    var selected by remember(index) { mutableStateOf<Int?>(null) }
    var result by remember(index) { mutableStateOf<AnswerRecord?>(null) }

    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember {
        TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
        }
    }

    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speak() {
        if (!ttsReady || question == null) return
        tts.language = Locale.US
        tts.setSpeechRate(0.88f)
        tts.speak(
            question.prompt,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "petlingo-question-${question.id}"
        )
    }

    LaunchedEffect(question?.id, ttsReady) {
        if (question?.type == QuestionType.LISTENING && ttsReady) {
            speak()
        }
    }

    if (question == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("第 ${index + 1} / $total 題", fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text("❤️ 20", color = Color(0xFFE64A3B), fontWeight = FontWeight.Bold)
        }

        LinearProgressIndicator(
            progress = { if (total == 0) 0f else (index + 1f) / total },
            modifier = Modifier.fillMaxWidth().height(6.dp)
        )

        Spacer(Modifier.height(20.dp))

        if (question.type == QuestionType.LISTENING) {
            Icon(
                Icons.Default.Headphones,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally).size(68.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            FilledTonalButton(
                onClick = ::speak,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .heightIn(min = 58.dp)
            ) {
                Icon(Icons.Default.VolumeUp, null)
                Spacer(Modifier.width(8.dp))
                Text("播放／重播")
            }
        } else {
            Text(
                question.prompt,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = ::speak,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.VolumeUp, "播放發音", tint = Color(0xFF1684E8))
            }
        }

        Spacer(Modifier.height(8.dp))

        question.options.forEachIndexed { optionIndex, option ->
            val submitted = result != null
            val correct = optionIndex == question.correctIndex
            val chosen = selected == optionIndex
            val background = when {
                submitted && correct -> Color(0xFF6FA968)
                submitted && chosen && !correct -> Color(0xFFF05045)
                else -> Color.Transparent
            }
            val foreground = if (submitted && (correct || chosen)) {
                Color.White
            } else {
                MaterialTheme.colorScheme.onSurface
            }

            Surface(
                modifier = Modifier.fillMaxWidth()
                    .clickable(enabled = !submitted) {
                        selected = optionIndex
                        onSelect(optionIndex)
                        result = onSubmit(optionIndex)
                    },
                shape = RoundedCornerShape(28.dp),
                color = background,
                border = if (!submitted || (!correct && !chosen)) {
                    ButtonDefaults.outlinedButtonBorder
                } else {
                    null
                }
            ) {
                Row(
                    Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (submitted && (correct || chosen)) {
                        Icon(
                            if (correct) Icons.Default.CheckCircle else Icons.Default.Close,
                            null,
                            tint = Color.White
                        )
                        Spacer(Modifier.width(10.dp))
                    }
                    Text(
                        option,
                        modifier = Modifier.weight(1f),
                        color = foreground,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        result?.let { answer ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (answer.isCorrect)
                        Color(0xFFE5F3DF)
                    else
                        Color(0xFFFFE3DF)
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            if (answer.isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                            null,
                            tint = if (answer.isCorrect) Color(0xFF4B9254) else Color(0xFFD84B40)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (answer.isCorrect) "答對了！" else "不正確",
                            fontWeight = FontWeight.Bold,
                            color = if (answer.isCorrect) Color(0xFF3D7D46) else Color(0xFFD84B40)
                        )
                    }
                    if (!answer.isCorrect) {
                        Text("正確答案：${answer.correctAnswer}", fontWeight = FontWeight.Bold)
                    }
                    Text(question.explanation)
                    Text(
                        "作答時間：${AnalyticsCalculator.seconds(answer.elapsedMillis)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(
                onClick = { if (!onNext()) onFinished() },
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (index + 1 >= total) "完成並查看結果" else "下一題")
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}
