package com.petlingo.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import java.util.Calendar
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.NavDestination.Companion.hierarchy
import com.petlingo.app.PetLingoViewModel
import com.petlingo.app.model.QuizMode
import com.petlingo.app.data.SettingsStore
import com.petlingo.app.ui.screens.*

@Composable
fun PetLingoApp(settingsStore: SettingsStore, vm: PetLingoViewModel = viewModel()) {
    val nav = rememberNavController()
    val sessions by vm.sessions.collectAsState()
    val readingSessions by vm.readingSessions.collectAsState()
    val currentCompletedSession by vm.currentCompletedSession.collectAsState()
    val analysisSession by vm.analysisSession.collectAsState()
    val questions by vm.questions.collectAsState()
    val currentIndex by vm.currentIndex.collectAsState()
    val favorites by vm.favorites.collectAsState()
    val query by vm.query.collectAsState()
    val wrongAnswers by vm.wrongAnswers.collectAsState()
    val speakingRecords by vm.speakingRecords.collectAsState()
    val appSettings by settingsStore.settings.collectAsState()

    val startOfToday = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    val todayAnswered = sessions
        .filter { it.finishedAt >= startOfToday }
        .sumOf { it.questionCount }

    val bottomItems = listOf(
        Triple("home", Icons.Default.Home, "首頁"),
        Triple("vocabulary", Icons.Default.MenuBook, "學習"),
        Triple("quizSetup", Icons.Default.Quiz, "測驗"),
        Triple("listeningSetup", Icons.Default.Headphones, "聽力"),
        Triple("speaking", Icons.Default.RecordVoiceOver, "口說")
    )

    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val hideBottomBar = currentRoute == "quiz" || currentRoute == "result" || currentRoute?.startsWith("reading/") == true

    Scaffold(bottomBar = {
        if (!hideBottomBar) NavigationBar {
            bottomItems.forEach { (route, icon, label) ->
                NavigationBarItem(false, { nav.navigate(route) { launchSingleTop = true } }, { Icon(icon, null) }, label = { Text(label) })
            }
        }
    }) { padding ->
        NavHost(nav, "home", Modifier.padding(padding)) {
            composable("home") {
                HomeScreen(
                    last = sessions.firstOrNull(),
                    favoriteCount = favorites.size,
                    todayAnswered = todayAnswered,
                    onQuiz = { nav.navigate("quizSetup") },
                    onVocabulary = { nav.navigate("vocabulary") },
                    onPhrase = { nav.navigate("phrases") },
                    onMock = { if (vm.newQuiz(20, QuizMode.TOEIC_MOCK)) nav.navigate("quiz") },
                    onAnalytics = { sessions.firstOrNull()?.let(vm::openAnalysis); nav.navigate("analytics") },
                    onHistory = { nav.navigate("history") },
                    onReading = { nav.navigate("reading") },
                    onWrongAnswers = { nav.navigate("wrongAnswers") },
                    onSpeaking = { nav.navigate("speaking") },
                    onListening = { nav.navigate("listeningSetup") },
                    onFavorites = { nav.navigate("favorites") },
                    onDailyMission = { nav.navigate("dailyMission") },
                    onAchievements = { nav.navigate("achievements") },
                    onSettings = { nav.navigate("settings") }
                )
            }
            composable("vocabulary") { VocabularyScreen(vm.filteredWords(), query, favorites, vm::setQuery, vm::toggleFavorite) }
            composable("phrases") { PhraseScreen(vm.phrases) }
            composable("quizSetup") { QuizSetupScreen(favorites.size, vm::newQuiz) { nav.navigate("quiz") } }
            composable("listeningSetup") { ListeningSetupScreen(vm::newQuiz) { nav.navigate("quiz") } }
            composable("quiz") { QuizScreen(questions.getOrNull(currentIndex), currentIndex, questions.size, vm::select, vm::submit, vm::next) { nav.navigate("result") } }
            composable("result") { ResultScreen(currentCompletedSession, { nav.navigate("analytics") }, { nav.navigate("home") }) }
            composable("reading") { ReadingScreen(vm.readingPassages) { nav.navigate("reading/$it") } }
            composable("reading/{id}") { entry ->
                val id = entry.arguments?.getString("id")?.toIntOrNull() ?: -1
                ReadingDetailScreen(vm.readingPassage(id), vm::addReadingWrong, vm::saveReadingSession)
            }
            composable("speaking") { SpeakingScreen(vm.words, vm.phrases, speakingRecords, vm::saveSpeakingRecord, vm::clearSpeakingHistory) }
            composable("wrongAnswers") { WrongAnswersScreen(wrongAnswers, vm::removeWrongAnswer, vm::clearWrongAnswers) }
            composable("analytics") { AnalyticsScreen(analysisSession) }
            composable("history") { HistoryScreen(sessions, readingSessions, { vm.openAnalysis(it); nav.navigate("analytics") }, vm::clearHistory) }
            composable("favorites") { FavoritesScreen(vm.words, favorites, vm::toggleFavorite) }
            composable("dailyMission") { DailyMissionScreen(todayAnswered) {
                if (vm.newQuiz(20, QuizMode.ENGLISH_TO_CHINESE)) nav.navigate("quiz")
            } }
            composable("achievements") { AchievementsScreen(sessions.size, todayAnswered) }
            composable("settings") {
                SettingsScreen(
                    settings = appSettings,
                    onUpdate = settingsStore::update,
                    sessionCount = sessions.size + readingSessions.size,
                    favoriteCount = favorites.size,
                    wrongAnswerCount = wrongAnswers.size,
                    speakingCount = speakingRecords.size,
                    onClearHistory = vm::clearHistory,
                    onClearWrongAnswers = vm::clearWrongAnswers,
                    onClearSpeaking = vm::clearSpeakingHistory,
                    onResetSettings = settingsStore::reset
                )
            }
        }
    }
}
