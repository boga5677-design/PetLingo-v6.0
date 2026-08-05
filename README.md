# PetLingo v6.0 Stable Foundation

v6.0 採用乾淨、單一路徑的 Android 專案骨架，保留目前已完成的功能，並移除舊版反覆修補所留下的建置腳本與會員程式碼。

## 已包含

- 主頁功能儀表板
- TOEIC 單字與片語
- 英翻中與中翻英
- 10／20／40 題模式
- 聽力測驗
- 閱讀測驗
- 多益模擬題
- 口說練習
- 錯題本
- 學習紀錄與答題時間分析
- 收藏、每日任務、成就與設定
- 三隻寵物主視覺與桌面圖示

## 固定建置環境

- JDK 17
- Gradle 8.11.1
- Android Gradle Plugin 8.7.3
- Kotlin 2.0.21
- compileSdk / targetSdk 35
- minSdk 26

## GitHub 上傳

將本資料夾內的內容上傳到 Repository 根目錄。根目錄必須直接看到：

```text
.github/
app/
gradle/
build.gradle.kts
settings.gradle.kts
gradle.properties
gradlew
gradlew.bat
```

GitHub Actions 會執行：

```bash
./gradlew --no-daemon :app:assembleDebug --stacktrace
```

APK 會出現在：

```text
app/build/outputs/apk/debug/app-debug.apk
```

本環境未安裝 Android SDK，因此 ZIP 已做結構、Kotlin 檔案與 YAML 靜態檢查，但尚未在此處完成實際 APK 編譯。
