@Composable
fun SettingsScreen(
    settings: com.petlingo.app.data.AppSettings,
    onUpdate: (com.petlingo.app.data.AppSettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 18.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "設定",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "調整發音、測驗、顯示與學習偏好。",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            SettingsSection(
                title = "發音與聲音",
                icon = Icons.Default.VolumeUp
            ) {
                Text(
                    text = "預設發音",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf("美式", "英式"),
                    selected = settings.accent,
                    onSelected = {
                        onUpdate(
                            settings.copy(accent = it)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "語音速度：${
                        String.format(
                            "%.2f",
                            settings.speechRate
                        )
                    }×"
                )

                Slider(
                    value = settings.speechRate,
                    onValueChange = {
                        onUpdate(
                            settings.copy(speechRate = it)
                        )
                    },
                    valueRange = 0.70f..1.30f,
                    steps = 5
                )

                SettingSwitch(
                    title = "按鈕與答題音效",
                    description = "播放介面操作、答對與答錯提示音。",
                    checked = settings.soundEffects,
                    onCheckedChange = {
                        onUpdate(
                            settings.copy(soundEffects = it)
                        )
                    }
                )

                SettingSwitch(
                    title = "題目自動朗讀",
                    description = "進入單字或聽力題目時，自動播放英文。",
                    checked = settings.autoReadQuestion,
                    onCheckedChange = {
                        onUpdate(
                            settings.copy(autoReadQuestion = it)
                        )
                    }
                )
            }
        }

        item {
            SettingsSection(
                title = "測驗與學習",
                icon = Icons.Default.Quiz
            ) {
                Text(
                    text = "預設題數",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf("10", "20", "40"),
                    selected = settings.defaultQuestionCount.toString(),
                    onSelected = {
                        onUpdate(
                            settings.copy(
                                defaultQuestionCount = it.toInt()
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "預設單字級數",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf(
                        "全部",
                        "初級",
                        "中級",
                        "中高級"
                    ),
                    selected = settings.defaultLevel,
                    onSelected = {
                        onUpdate(
                            settings.copy(defaultLevel = it)
                        )
                    }
                )

                SettingSwitch(
                    title = "答題後顯示解析",
                    description = "顯示正確答案、中文意思與題目說明。",
                    checked = settings.showExplanation,
                    onCheckedChange = {
                        onUpdate(
                            settings.copy(showExplanation = it)
                        )
                    }
                )

                SettingSwitch(
                    title = "錯題自動加入錯題本",
                    description = "答錯後自動保存，方便之後複習。",
                    checked = settings.addWrongAnswerAutomatically,
                    onCheckedChange = {
                        onUpdate(
                            settings.copy(
                                addWrongAnswerAutomatically = it
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "每日任務目標",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf("10", "20", "40"),
                    selected = settings.dailyGoal.toString(),
                    onSelected = {
                        onUpdate(
                            settings.copy(
                                dailyGoal = it.toInt()
                            )
                        )
                    }
                )
            }
        }

        item {
            SettingsSection(
                title = "顯示與提醒",
                icon = Icons.Default.Palette
            ) {
                Text(
                    text = "顯示模式",
                    fontWeight = FontWeight.Bold
                )

                ChoiceRow(
                    choices = listOf(
                        "系統",
                        "淺色",
                        "深色"
                    ),
                    selected = settings.themeMode,
                    onSelected = {
                        onUpdate(
                            settings.copy(themeMode = it)
                        )
                    }
                )

                SettingSwitch(
                    title = "大型文字",
                    description = "增加設定與學習頁面的文字可讀性。",
                    checked = settings.largeText,
                    onCheckedChange = {
                        onUpdate(
                            settings.copy(largeText = it)
                        )
                    }
                )

                SettingSwitch(
                    title = "每日學習提醒",
                    description = "保存每日學習提醒偏好。",
                    checked = settings.dailyReminder,
                    onCheckedChange = {
                        onUpdate(
                            settings.copy(dailyReminder = it)
                        )
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider()

            content()
        }
    }
}

@Composable
private fun ChoiceRow(
    choices: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(6.dp)
    ) {
        choices.forEach { value ->
            FilterChip(
                selected = selected == value,
                onClick = {
                    onSelected(value)
                },
                label = {
                    Text(
                        text = value,
                        maxLines = 1
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = description,
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
