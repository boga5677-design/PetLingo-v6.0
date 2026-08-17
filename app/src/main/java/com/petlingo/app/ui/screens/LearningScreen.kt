package com.petlingo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LearningScreen(
    onVocabulary: () -> Unit,
    onPhrase: () -> Unit,
    onListeningPractice: () -> Unit,
    onSpeakingPractice: () -> Unit,
    onPronunciationInput: () -> Unit
) {
    Column(
        Modifier.fillMaxSize().padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("學習", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("所有學習功能都支援美式與英式英文發音。")

        LearningCard("英文單字學習", "每個單字皆有美式／英式發音", Icons.Default.MenuBook, onVocabulary)
        LearningCard("片語學習", "片語與例句皆有美式／英式發音", Icons.Default.Chat, onPhrase)
        LearningCard("聽力練習", "同一單字比較美式／英式發音", Icons.Default.Headphones, onListeningPractice)
        LearningCard("口說練習", "可切換美式／英式示範後跟讀", Icons.Default.Mic, onSpeakingPractice)
        LearningCard("英文自由朗讀", "輸入任意英文，播放美式／英式發音", Icons.Default.RecordVoiceOver, onPronunciationInput)
    }
}

@Composable
private fun LearningCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
