package com.petlingo.app.data

import android.content.Context
import com.petlingo.app.model.Word

class WordRepository(private val context: Context) {
    fun loadWords(limit: Int = 8000): List<Word> = runCatching {
        context.assets.open("toeic_words.tsv").bufferedReader().useLines { lines ->
            lines.drop(1).mapNotNull { line ->
                val p = line.split('\t')
                if (p.size >= 3) Word(p[0].toIntOrNull() ?: 0, p[1], p[2]) else null
            }.take(limit).toList()
        }
    }.getOrDefault(emptyList())
}
