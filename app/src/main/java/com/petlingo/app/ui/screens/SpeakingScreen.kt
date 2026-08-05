package com.petlingo.app.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.petlingo.app.model.SpeakingRecord
import com.petlingo.app.model.Word
import com.petlingo.app.util.SpeakingScorer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SpeakingScreen(
    words: List<Word>,
    records: List<SpeakingRecord>,
    onSave: (SpeakingRecord) -> Unit,
    onClear: () -> Unit
) {
    val context = LocalContext.current
    var target by remember { mutableStateOf(words.firstOrNull()?.english.orEmpty()) }
    var recognized by remember { mutableStateOf("") }
    var accent by remember { mutableStateOf("美式") }
    var score by remember { mutableIntStateOf(-1) }
    var status by remember { mutableStateOf("先播放示範，再按麥克風跟讀。") }
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember { TextToSpeech(context) { ttsReady = it == TextToSpeech.SUCCESS } }

    DisposableEffect(Unit) { onDispose { tts.stop(); tts.shutdown() } }
    LaunchedEffect(accent, ttsReady) {
        if (ttsReady) tts.language = if (accent == "英式") Locale.UK else Locale.US
    }

    fun saveResult(text: String) {
        recognized = text
        score = SpeakingScorer.score(target, text)
        status = SpeakingScorer.feedback(target, text, score)
        onSave(SpeakingRecord(targetText = target.trim(), recognizedText = text, score = score, accent = accent))
    }

    val speechLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val text = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull().orEmpty()
            saveResult(text)
        } else status = "未取得語音辨識結果，可再試一次。"
    }

    fun startRecognition() {
        val locale = if (accent == "英式") "en-GB" else "en-US"
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "請朗讀：$target")
        }
        runCatching { speechLauncher.launch(intent) }
            .onFailure { status = "裝置目前沒有可用的語音辨識服務。" }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) startRecognition() else status = "需要麥克風權限才能進行口說辨識。"
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("口說練習", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("使用系統語音辨識比較朗讀文字；分數代表文字辨識相似度，不是音素級專業發音評測。")
        }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = target,
                        onValueChange = { target = it; score = -1; recognized = "" },
                        label = { Text("練習單字或句子") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = false
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(selected = accent == "美式", onClick = { accent = "美式" }, label = { Text("美式") })
                        FilterChip(selected = accent == "英式", onClick = { accent = "英式" }, label = { Text("英式") })
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {
                                if (target.isNotBlank() && ttsReady) {
                                    tts.speak(target, TextToSpeech.QUEUE_FLUSH, null, "petlingo-demo")
                                } else status = "語音示範尚未準備完成。"
                            },
                            enabled = target.isNotBlank()
                        ) { Icon(Icons.Default.VolumeUp, null); Text("播放示範") }
                        Button(
                            onClick = {
                                if (target.isBlank()) return@Button
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                    startRecognition()
                                } else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            },
                            enabled = target.isNotBlank()
                        ) { Icon(Icons.Default.Mic, null); Text("開始朗讀") }
                    }
                    if (recognized.isNotBlank()) Text("辨識結果：$recognized")
                    if (score >= 0) Text("本次相似度：$score 分", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(status)
                }
            }
        }
        item {
            Text("快速練習", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(words.take(8).joinToString("　") { it.english })
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("最近口說紀錄", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                if (records.isNotEmpty()) IconButton(onClick = onClear) { Icon(Icons.Default.Delete, "清除口說紀錄") }
            }
        }
        if (records.isEmpty()) item { Text("尚無口說紀錄。") }
        items(records.take(30), key = { it.id }) { record ->
            SpeakingRecordCard(record)
        }
    }
}

@Composable
private fun SpeakingRecordCard(record: SpeakingRecord) {
    val date = remember(record.createdAt) {
        SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(Date(record.createdAt))
    }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(record.targetText, fontWeight = FontWeight.Bold)
            Text("辨識：${record.recognizedText.ifBlank { "無結果" }}")
            Text("${record.accent}・${record.score} 分・$date")
        }
    }
}
