# PetLingo v6.0.1 Compile Fix

- 刪除未使用且資料模型不相容的 `PetData.kt`。
- 修正 `Word` 與 `QuizQuestion` unresolved reference 編譯錯誤。
- 保留正式使用中的 `model/Models.kt`。
- GitHub Actions 建置前會移除舊 `PetData.kt`。
- 新增資料模型一致性檢查。
- versionCode 更新為 21。
- versionName 更新為 6.0.1-compile-fix。
