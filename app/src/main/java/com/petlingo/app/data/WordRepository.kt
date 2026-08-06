package com.petlingo.app.data

import android.content.Context
import com.petlingo.app.model.Word

class WordRepository(private val context: Context) {
    fun loadWords(limit: Int = 10000): List<Word> = runCatching {
        context.assets.open("gept_words.tsv").bufferedReader().useLines { lines ->
            lines.drop(1).mapNotNull { line ->
                val p = line.split('	')
                if (p.size < 7) return@mapNotNull null
                Word(
                    id = p[0].toIntOrNull() ?: return@mapNotNull null,
                    english = p[1].trim(),
                    chinese = p[2].trim(),
                    partOfSpeech = p[3].trim(),
                    note = p[4].trim(),
                    level = p[5].trim(),
                    academic = p[6].trim()
                )
            }.filter { it.english.isNotBlank() && it.chinese.isNotBlank() }
                .take(limit)
                .toList()
        }
    }.getOrElse {
        listOf(
            Word(1, "ability", "能力、才能", "noun", level = "初級"),
            Word(2, "abandon", "拋棄、捨棄、中止", "verb", level = "中級"),
            Word(3, "abbreviate", "縮寫、使簡短", "verb", level = "中高級"),
            Word(4, "available", "有空的、可獲得的", "adj.", level = "初級")
        )
    }
}
