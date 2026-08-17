package com.petlingo.app.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun PronunciationInputScreen() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var ready by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("可輸入單字庫以外的英文單字、片語或句子。") }

    val tts = remember {
        TextToSpeech(context) { ready = it == TextToSpeech.SUCCESS }
    }

    DisposableEffect(tts) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speak(locale: Locale, label: String) {
        val target = text.trim()
        if (target.isBlank()) {
            message = "請先輸入英文。"
            return
        }
        if (!ready) {
            message = "語音引擎尚未準備完成。"
            return
        }

        val result = tts.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            message = "手機沒有${label}英文語音，請先下載對應 Android TTS 語音。"
            return
        }

        tts.setSpeechRate(0.88f)
        tts.speak(
            target,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "free-${locale.toLanguageTag()}-${System.currentTimeMillis()}"
        )
        message = "正在播放${label}英文。"
    }

    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Icon(Icons.Default.RecordVoiceOver, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
            Text("英文自由朗讀", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
            label = { Text("輸入英文單字、片語或句子") },
            placeholder = { Text("例如：The weather is beautiful today.") },
            trailingIcon = {
                if (text.isNotBlank()) {
                    IconButton(onClick = { text = "" }) {
                        Icon(Icons.Default.Clear, "清除")
                    }
                }
            }
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = { speak(Locale.US, "美式") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.VolumeUp, null)
                Spacer(Modifier.width(6.dp))
                Text("美式發音")
            }

            OutlinedButton(
                onClick = { speak(Locale.UK, "英式") },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.VolumeUp, null)
                Spacer(Modifier.width(6.dp))
                Text("英式發音")
            }
        }

        Text(message, style = MaterialTheme.typography.bodySmall)
    }
}
