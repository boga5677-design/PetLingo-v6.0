package com.petlingo.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Scheme = lightColorScheme(
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

@Composable
fun PetLingoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, content = content)
}
