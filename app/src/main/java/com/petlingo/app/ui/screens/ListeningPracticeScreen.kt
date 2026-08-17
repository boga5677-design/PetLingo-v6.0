package com.petlingo.app.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.petlingo.app.model.Word
import java.util.Locale

@Composable
fun ListeningPracticeScreen(words: List<Word>) {
    var word by remember(words) { mutableStateOf(words.randomOrNull()) }
    var showAnswer by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var ready by remember { mutableStateOf(false) }
    val tts = remember { TextToSpeech(context) { ready = it == TextToSpeech.SUCCESS } }

    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun play(locale: Locale, id: String) {
        val current = word ?: return
        if (!ready) return
        tts.language = locale
        tts.setSpeechRate(0.88f)
        tts.speak(current.english, TextToSpeech.QUEUE_FLUSH, null, id)
    }

    fun next() {
        val old = word?.id
        word = words.shuffled().firstOrNull { it.id != old } ?: words.randomOrNull()
        showAnswer = false
    }

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("聽力練習", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("同一個單字可分別播放美式與英式發音。", textAlign = TextAlign.Center)

        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                Modifier.fillMaxWidth().padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { play(Locale.US, "listening-us-${word?.id}") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.VolumeUp, null)
                        Spacer(Modifier.width(6.dp))
                        Text("美式發音")
                    }

                    OutlinedButton(
                        onClick = { play(Locale.UK, "listening-uk-${word?.id}") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.VolumeUp, null)
                        Spacer(Modifier.width(6.dp))
                        Text("英式發音")
                    }
                }

                if (showAnswer) {
                    Text(
                        word?.english.orEmpty(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        word?.chinese.orEmpty(),
                        style = MaterialTheme.typography.titleMedium
                    )
                } else {
                    OutlinedButton(
                        onClick = { showAnswer = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Visibility, null)
                        Spacer(Modifier.width(8.dp))
                        Text("顯示答案")
                    }
                }

                OutlinedButton(
                    onClick = ::next,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Refresh, null)
                    Spacer(Modifier.width(8.dp))
                    Text("下一個")
                }
            }
        }
    }
}
