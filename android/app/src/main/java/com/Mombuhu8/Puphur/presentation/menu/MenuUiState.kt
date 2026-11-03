package com.Mombuhu8.Puphur.presentation.menu

import com.Mombuhu8.Puphur.domain.model.AppSettings
import com.Mombuhu8.Puphur.domain.model.DailyEntry
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.UnlockKind

enum class MenuPanel {
    NONE,
    MAP,
    RECORDS,
    COLLECTION,
    SETTINGS
}

data class StageEntry(
    val stageId: Int,
    val regionKey: String,
    val goalFloors: Int,
    val stars: Int,
    val unlocked: Boolean
)

data class UnlockEntry(
    val key: String,
    val kind: UnlockKind,
    val starCost: Int,
    val unlocked: Boolean
)

data class RecordsSummary(
    val regionsCleared: Int = 0,
    val bestScore: Int = 0,
    val bestAccuracy: Int = 0,
    val longestStreak: Int = 0,
    val daily: List<DailyEntry> = emptyList()
)

data class MenuUiState(
    val loading: Boolean = true,
    val totalStars: Int = 0,
    val selectedMode: GameMode = GameMode.CAMPAIGN,
    val nextStageId: Int = 1,
    val nextStageRegionKey: String = "harbour_spires",
    val nextStageGoal: Int = 12,
    val nextStageStars: Int = 0,
    val stages: List<StageEntry> = emptyList(),
    val unlocks: List<UnlockEntry> = emptyList(),
    val records: RecordsSummary = RecordsSummary(),
    val settings: AppSettings = AppSettings(),
    val panel: MenuPanel = MenuPanel.NONE,
    val confirmingReset: Boolean = false
)
