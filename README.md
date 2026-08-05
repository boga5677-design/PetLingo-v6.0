# PetLingo v6.0.1 Compile Fix

這一版修正 GitHub Actions 的 Kotlin 編譯錯誤：

```text
PetData.kt: Unresolved reference 'Word'
PetData.kt: Unresolved reference 'QuizQuestion'
```

原因是 `PetData.kt` 屬於舊版範例資料，使用三參數 `Word(...)` 與舊格式 `QuizQuestion(...)`，
但目前正式資料模型已改為 `model/Models.kt` 內的新版結構，而且專案中沒有任何功能使用 `PetData`。

因此本版直接移除該檔案，而不是建立第二套重複 Model。

GitHub Actions 會執行：

```bash
./gradlew --no-daemon :app:assembleDebug --stacktrace
```

APK 成功後位於：

```text
app/build/outputs/apk/debug/app-debug.apk
```
