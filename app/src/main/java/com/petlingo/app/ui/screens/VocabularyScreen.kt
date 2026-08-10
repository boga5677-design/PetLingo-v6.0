package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.StudyNote
import com.petlingo.app.model.Word

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
    var selectedLevel by remember { mutableStateOf("全部") }
    val shown = remember(words, selectedLevel) {
        if (selectedLevel == "全部") words else words.filter { it.level == selectedLevel }
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("GEPT 單字庫", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("共 ${shown.size} 筆符合結果・已收藏 ${favorites.size} 個")
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("搜尋英文或中文") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("全部", "初級", "中級", "中高級")) { level ->
                FilterChip(
                    selected = selectedLevel == level,
                    onClick = { selectedLevel = level },
                    label = { Text(level) }
                )
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(shown, key = { it.id }) { word ->
                val noteKey = "vocab-${word.id}"
                Card(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(word.english, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                if (word.partOfSpeech.isNotBlank()) {
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        word.partOfSpeech,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Text(word.chinese)
                            Text(
                                buildString {
                                    append(word.level)
                                    if (word.academic.isNotBlank()) append("・學術字彙 ${word.academic}")
                                },
                                style = MaterialTheme.typography.labelSmall,
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

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                                word.note
                                            ).filter { it.isNotBlank() }.joinToString("・")
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    if (noteKey in noteKeys) Icons.Default.Star else Icons.Default.StarBorder,
                                    if (noteKey in noteKeys) "移除筆記" else "加入我的筆記",
                                    tint = if (noteKey in noteKeys) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }

                            IconButton(onClick = { onToggleFavorite(word.id) }) {
                                Icon(
                                    if (word.id in favorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    if (word.id in favorites) "取消收藏" else "加入收藏"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
