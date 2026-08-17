package com.petlingo.app.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.Phrase
import java.util.Locale

@Composable
fun PhraseScreen(phrases: List<Phrase>) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember {
        TextToSpeech(context) { ttsReady = it == TextToSpeech.SUCCESS }
    }

    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speak(text: String, locale: Locale, id: String) {
        if (!ttsReady || text.isBlank()) return
        tts.language = locale
        tts.setSpeechRate(0.88f)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
    }

    val shown = remember(query, phrases) {
        if (query.isBlank()) phrases
        else phrases.filter {
            it.english.contains(query, true) || it.chinese.contains(query)
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("多益片語", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("常用商務、旅遊與職場片語，共 ${phrases.size} 組")

        OutlinedTextField(
            query,
            { query = it },
            Modifier.fillMaxWidth(),
            label = { Text("搜尋片語或中文") },
            singleLine = true
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(shown, key = { it.id }) { phrase ->
                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            phrase.english,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { speak(phrase.english, Locale.US, "phrase-us-${phrase.id}") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.VolumeUp, null)
                                Spacer(Modifier.width(4.dp))
                                Text("美式")
                            }

                            OutlinedButton(
                                onClick = { speak(phrase.english, Locale.UK, "phrase-uk-${phrase.id}") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.VolumeUp, null)
                                Spacer(Modifier.width(4.dp))
                                Text("英式")
                            }
                        }

                        Text(phrase.chinese, color = MaterialTheme.colorScheme.primary)
                        HorizontalDivider()

                        Text("例句", fontWeight = FontWeight.Bold)
                        Text(phrase.example)

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextButton(
                                onClick = { speak(phrase.example, Locale.US, "example-us-${phrase.id}") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.VolumeUp, null)
                                Text("例句美式")
                            }
                            TextButton(
                                onClick = { speak(phrase.example, Locale.UK, "example-uk-${phrase.id}") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.VolumeUp, null)
                                Text("例句英式")
                            }
                        }
                    }
                }
            }
        }
    }
}
