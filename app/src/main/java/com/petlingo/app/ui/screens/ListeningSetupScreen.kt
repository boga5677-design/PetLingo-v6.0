package com.petlingo.app.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.QuizMode
import com.petlingo.app.model.Word
import java.util.Locale

@Composable
fun ListeningSetupScreen(
    words: List<Word>,
    onStart: (Int, QuizMode) -> Boolean,
    onReady: () -> Unit
) {
    var count by remember { mutableIntStateOf(20) }
    var warning by remember { mutableStateOf<String?>(null) }

    var practiceWord by remember(words) {
        mutableStateOf(words.randomOrNull())
    }
    var revealMeaning by remember { mutableStateOf(false) }

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

    fun playPracticeWord() {
        val word = practiceWord ?: return
        if (!ttsReady) return
        tts.language = Locale.US
        tts.setSpeechRate(0.88f)
        tts.speak(
            word.english,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "petlingo-listening-practice-${word.id}"
        )
    }

    fun nextPracticeWord() {
        if (words.isEmpty()) return
        val oldId = practiceWord?.id
        var next = words.randomOrNull()
        repeat(8) {
            if (next?.id != oldId || words.size <= 1) return@repeat
            next = words.randomOrNull()
        }
        practiceWord = next
        revealMeaning = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Icon(
            Icons.Default.Headphones,
            contentDescription = null,
            modifier = Modifier.size(62.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            "聽力",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    "聽力練習",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "先聽發音，再確認中文意思。",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                FilledTonalButton(
                    onClick = ::playPracticeWord,
                    enabled = practiceWord != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.VolumeUp, null)
                    Spacer(Modifier.width(8.dp))
                    Text("播放單字")
                }

                if (revealMeaning) {
                    Text(
                        practiceWord?.english.orEmpty(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        practiceWord?.chinese.orEmpty(),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                } else {
                    OutlinedButton(
                        onClick = { revealMeaning = true },
                        enabled = practiceWord != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Visibility, null)
                        Spacer(Modifier.width(8.dp))
                        Text("顯示答案")
                    }
                }

                OutlinedButton(
                    onClick = ::nextPracticeWord,
                    enabled = words.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(Modifier.width(8.dp))
                    Text("下一個練習")
                }
            }
        }

        HorizontalDivider()

        Text(
            "聽力測驗",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            "題數",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(10, 20, 40).forEach { option ->
                FilterChip(
                    selected = count == option,
                    onClick = { count = option },
                    label = { Text("$option 題") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        warning?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                if (onStart(count, QuizMode.LISTENING)) {
                    onReady()
                } else {
                    warning = "題庫不足，無法建立聽力測驗。"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 54.dp)
        ) {
            Text("開始聽力測驗")
        }
    }
}
