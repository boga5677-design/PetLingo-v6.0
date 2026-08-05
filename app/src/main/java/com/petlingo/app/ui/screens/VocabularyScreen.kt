package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.petlingo.app.model.Word

@Composable
fun VocabularyScreen(
    words: List<Word>,
    query: String,
    favorites: Set<Int>,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (Int) -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("單字庫", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("共 ${words.size} 筆符合結果・已收藏 ${favorites.size} 個")
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("搜尋英文或中文") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(words, key = { it.id }) { word ->
                Card(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(word.english, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(word.chinese)
                        }
                        IconButton(onClick = { onToggleFavorite(word.id) }) {
                            Icon(
                                if (word.id in favorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (word.id in favorites) "取消收藏" else "加入收藏"
                            )
                        }
                    }
                }
            }
        }
    }
}
