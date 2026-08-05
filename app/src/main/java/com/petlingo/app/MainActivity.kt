package com.petlingo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.petlingo.app.ui.PetLingoApp
import com.petlingo.app.ui.theme.PetLingoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent { PetLingoTheme { PetLingoApp() } }
    }
}
