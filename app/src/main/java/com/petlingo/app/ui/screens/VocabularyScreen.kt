package com.petlingo.app.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.StudyNote
import com.petlingo.app.model.Word
import java.util.Locale

@Composable
fun VocabularyScreen(
    words: List<Word>,
    query: String,
    favorites: Set<Int>,
    noteKeys: Set<String>,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onToggleNote: (StudyNote) -> Unit
) {
    val context = LocalContext.current
    var ttsReady by remember { mutableStateOf(false) }
    var ttsMessage by remember { mutableStateOf<String?>(null) }

    val tts = remember {
        TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
            if (!ttsReady) ttsMessage = "手機語音引擎尚未準備完成。"
        }
    }

    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speak(text: String, locale: Locale, accentName: String, id: String) {
        if (!ttsReady || text.isBlank()) {
            ttsMessage = "語音引擎尚未準備完成。"
            return
        }
        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            ttsMessage = "手機目前沒有${accentName}英文語音，請至 Android 文字轉語音設定下載對應語音。"
            return
        }
        tts.setSpeechRate(0.88f)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
        ttsMessage = null
    }

    var selectedLevel by remember { mutableStateOf("全部") }

    val shown = remember(words, selectedLevel) {
        when {
            selectedLevel == "全部" -> words
            selectedLevel.startsWith("高中") -> {
                val ceec = selectedLevel.removePrefix("高中").removeSuffix("級")
                words.filter { it.ceecLevel == ceec }
            }
            else -> words.filter { it.level == selectedLevel }
        }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("英文單字庫", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("共 ${shown.size} 筆符合結果・已收藏 ${favorites.size} 個")

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("搜尋英文或中文") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        ttsMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("全部", "初級", "中級", "中高級", "高中1級", "高中2級", "高中3級", "高中4級", "高中5級", "高中6級")) { level ->
                FilterChip(
                    selected = selectedLevel == level,
                    onClick = { selectedLevel = level },
                    label = { Text(level) }
                )
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(shown, key = { it.id }) { word ->
                val noteKey = "vocab-${word.id}"

                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    word.english,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )

                                if (word.partOfSpeech.isNotBlank()) {
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        word.partOfSpeech,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    onToggleNote(
                                        StudyNote(
                                            key = noteKey,
                                            category = "單字庫",
                                            kind = "單字",
                                            title = word.english,
                                            content = word.chinese,
                                            detail = listOf(
                                                word.partOfSpeech,
                                                word.level,
                                                if (word.ceecLevel.isNotBlank()) "高中 ${word.ceecLevel} 級" else "",
                                                word.note
                                            ).filter { it.isNotBlank() }.joinToString("・")
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    if (noteKey in noteKeys) Icons.Default.Star else Icons.Default.StarBorder,
                                    if (noteKey in noteKeys) "移除筆記" else "加入我的筆記"
                                )
                            }

                            IconButton(onClick = { onToggleFavorite(word.id) }) {
                                Icon(
                                    if (word.id in favorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    if (word.id in favorites) "取消收藏" else "加入收藏"
                                )
                            }
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    speak(word.english, Locale.US, "美式", "vocab-us-${word.id}")
                                },
                                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                            ) {
                                Icon(Icons.Default.VolumeUp, null)
                                Spacer(Modifier.width(6.dp))
                                Text("美式發音")
                            }

                            OutlinedButton(
                                onClick = {
                                    speak(word.english, Locale.UK, "英式", "vocab-uk-${word.id}")
                                },
                                modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                            ) {
                                Icon(Icons.Default.VolumeUp, null)
                                Spacer(Modifier.width(6.dp))
                                Text("英式發音")
                            }
                        }

                        Text(word.chinese, style = MaterialTheme.typography.titleMedium)

                        Text(
                            buildString {
                                append(word.level)
                                if (word.ceecLevel.isNotBlank()) append("・高中 ${word.ceecLevel} 級")
                                if (word.academic.isNotBlank()) append("・學術字彙 ${word.academic}")
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        if (word.note.isNotBlank()) {
                            Text(
                                word.note,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
