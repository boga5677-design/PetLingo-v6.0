package com.petlingo.app.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightScheme = lightColorScheme(
    primary = Color(0xFF5B4296),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEAE1FF),
    onPrimaryContainer = Color(0xFF2D1E50),
    secondary = Color(0xFF73985F),
    secondaryContainer = Color(0xFFE4F2DA),
    tertiary = Color(0xFFE0A56D),
    background = Color(0xFFFFFBF3),
    surface = Color(0xFFFFFBF3),
    surfaceVariant = Color(0xFFF7F0E8),
    error = Color(0xFFD84B40),
    errorContainer = Color(0xFFFFDAD5)
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFFD1BCFF),
    primaryContainer = Color(0xFF473276),
    secondary = Color(0xFFB9D8A5),
    secondaryContainer = Color(0xFF344D2B),
    tertiary = Color(0xFFFFB77A),
    background = Color(0xFF17131C),
    surface = Color(0xFF17131C),
    surfaceVariant = Color(0xFF302A36)
)

@Composable
fun PetLingoTheme(themeMode: String = "系統", content: @Composable () -> Unit) {
    val useDark = when (themeMode) {
        "深色" -> true
        "淺色" -> false
        else -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (useDark) DarkScheme else LightScheme,
        content = content
    )
}
