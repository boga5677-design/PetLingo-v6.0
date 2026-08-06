package com.petlingo.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.petlingo.app.PetLingoViewModel
import com.petlingo.app.data.SettingsStore
import com.petlingo.app.model.QuizMode
import com.petlingo.app.ui.screens.AchievementsScreen
import com.petlingo.app.ui.screens.AnalyticsScreen
import com.petlingo.app.ui.screens.DailyMissionScreen
import com.petlingo.app.ui.screens.FavoritesScreen
import com.petlingo.app.ui.screens.HistoryScreen
import com.petlingo.app.ui.screens.HomeScreen
import com.petlingo.app.ui.screens.ListeningSetupScreen
import com.petlingo.app.ui.screens.PhraseScreen
import com.petlingo.app.ui.screens.QuizScreen
import com.petlingo.app.ui.screens.QuizSetupScreen
import com.petlingo.app.ui.screens.ReadingDetailScreen
import com.petlingo.app.ui.screens.ReadingScreen
import com.petlingo.app.ui.screens.ResultScreen
import com.petlingo.app.ui.screens.SettingsScreen
import com.petlingo.app.ui.screens.SpeakingScreen
import com.petlingo.app.ui.screens.VocabularyScreen
import com.petlingo.app.ui.screens.WrongAnswersScreen
import java.util.Calendar

@Composable
fun PetLingoApp(
    settingsStore: SettingsStore,
    vm: PetLingoViewModel = viewModel()
) {
    val navController = rememberNavController()

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
        .filter { session ->
            session.finishedAt >= startOfToday
        }
        .sumOf { session ->
            session.questionCount
        }

    val bottomItems = listOf(
        BottomNavigationItem(
            route = "home",
            icon = Icons.Default.Home,
            label = "首頁"
        ),
        BottomNavigationItem(
            route = "vocabulary",
            icon = Icons.Default.MenuBook,
            label = "學習"
        ),
        BottomNavigationItem(
            route = "quizSetup",
            icon = Icons.Default.Quiz,
            label = "測驗"
        ),
        BottomNavigationItem(
            route = "listeningSetup",
            icon = Icons.Default.Headphones,
            label = "聽力"
        ),
        BottomNavigationItem(
            route = "speaking",
            icon = Icons.Default.RecordVoiceOver,
            label = "口說"
        )
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val hideBottomBar =
        currentRoute == "quiz" ||
        currentRoute == "result" ||
        currentRoute?.startsWith("reading/") == true

    Scaffold(
        bottomBar = {
            if (!hideBottomBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true

                                    popUpTo("home") {
                                        saveState = true
                                    }

                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(item.label)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(
                    last = sessions.firstOrNull(),
                    favoriteCount = favorites.size,
                    todayAnswered = todayAnswered,
                    onQuiz = {
                        navController.navigate("quizSetup")
                    },
                    onVocabulary = {
                        navController.navigate("vocabulary")
                    },
                    onPhrase = {
                        navController.navigate("phrases")
                    },
                    onMock = {
                        val created = vm.newQuiz(
                            count = 20,
                            mode = QuizMode.TOEIC_MOCK
                        )

                        if (created) {
                            navController.navigate("quiz")
                        }
                    },
                    onAnalytics = {
                        sessions.firstOrNull()?.let { session ->
                            vm.openAnalysis(session)
                        }

                        navController.navigate("analytics")
                    },
                    onHistory = {
                        navController.navigate("history")
                    },
                    onReading = {
                        navController.navigate("reading")
                    },
                    onWrongAnswers = {
                        navController.navigate("wrongAnswers")
                    },
                    onSpeaking = {
                        navController.navigate("speaking")
                    },
                    onListening = {
                        navController.navigate("listeningSetup")
                    },
                    onFavorites = {
                        navController.navigate("favorites")
                    },
                    onDailyMission = {
                        navController.navigate("dailyMission")
                    },
                    onAchievements = {
                        navController.navigate("achievements")
                    },
                    onSettings = {
                        navController.navigate("settings")
                    }
                )
            }

            composable("vocabulary") {
                VocabularyScreen(
                    words = vm.filteredWords(),
                    query = query,
                    favorites = favorites,
                    onQueryChange = vm::setQuery,
                    onToggleFavorite = vm::toggleFavorite
                )
            }

            composable("phrases") {
                PhraseScreen(
                    phrases = vm.phrases
                )
            }

            composable("quizSetup") {
                QuizSetupScreen(
                    favoriteCount = favorites.size,
                    onStart = vm::newQuiz,
                    onReady = {
                        navController.navigate("quiz")
                    }
                )
            }

            composable("listeningSetup") {
                ListeningSetupScreen(
                    onStart = vm::newQuiz,
                    onReady = {
                        navController.navigate("quiz")
                    }
                )
            }

            composable("quiz") {
                QuizScreen(
                    question = questions.getOrNull(currentIndex),
                    currentIndex = currentIndex,
                    totalQuestions = questions.size,
                    onSelect = vm::select,
                    onSubmit = vm::submit,
                    onNext = vm::next,
                    onFinished = {
                        navController.navigate("result") {
                            popUpTo("quiz") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable("result") {
                ResultScreen(
                    session = currentCompletedSession,
                    onAnalytics = {
                        navController.navigate("analytics")
                    },
                    onHome = {
                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable("reading") {
                ReadingScreen(
                    passages = vm.readingPassages,
                    onOpen = { passageId ->
                        navController.navigate(
                            "reading/$passageId"
                        )
                    }
                )
            }

            composable("reading/{id}") { entry ->
                val id = entry.arguments
                    ?.getString("id")
                    ?.toIntOrNull()
                    ?: -1

                ReadingDetailScreen(
                    passage = vm.readingPassage(id),
                    onWrongAnswer = vm::addReadingWrong,
                    onSaveSession = vm::saveReadingSession
                )
            }

            composable("speaking") {
                SpeakingScreen(
                    words = vm.words,
                    phrases = vm.phrases,
                    records = speakingRecords,
                    onSave = vm::saveSpeakingRecord,
                    onClear = vm::clearSpeakingHistory
                )
            }

            composable("wrongAnswers") {
                WrongAnswersScreen(
                    wrongAnswers = wrongAnswers,
                    onRemove = vm::removeWrongAnswer,
                    onClear = vm::clearWrongAnswers
                )
            }

            composable("analytics") {
                AnalyticsScreen(
                    session = analysisSession
                )
            }

            composable("history") {
                HistoryScreen(
                    sessions = sessions,
                    readingSessions = readingSessions,
                    onOpenSession = { session ->
                        vm.openAnalysis(session)
                        navController.navigate("analytics")
                    },
                    onClear = vm::clearHistory
                )
            }

            composable("favorites") {
                FavoritesScreen(
                    words = vm.words,
                    favorites = favorites,
                    onToggle = vm::toggleFavorite
                )
            }

            composable("dailyMission") {
                DailyMissionScreen(
                    answered = todayAnswered,
                    onStartQuiz = {
                        val created = vm.newQuiz(
                            count = 20,
                            mode = QuizMode.ENGLISH_TO_CHINESE
                        )

                        if (created) {
                            navController.navigate("quiz")
                        }
                    }
                )
            }

            composable("achievements") {
                AchievementsScreen(
                    sessionCount = sessions.size,
                    answered = todayAnswered
                )
            }

            composable("settings") {
                SettingsScreen(
                    settings = appSettings,
                    onUpdate = settingsStore::update
                )
            }
        }
    }
}

private data class BottomNavigationItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)
