package com.Mombuhu8.Puphur.domain.model

enum class GameMode {
    CAMPAIGN,
    DAILY,
    PRACTICE
}

enum class UnlockKind {
    MATERIAL,
    DECORATION,
    THEME
}

enum class StageBrief {
    NONE,
    OVERLAP,
    ANCHOR,
    WIND,
    HITCH
}

object TowerRegions {
    const val HARBOUR = "harbour_spires"
    const val GLASS = "glass_quarter"
    const val SIGNAL = "signal_crown"
}

data class TowerRegion(
    val key: String,
    val firstStage: Int,
    val lastStage: Int
)

data class StageSpec(
    val stageId: Int,
    val regionKey: String,
    val goalFloors: Int,
    val baseSpeed: Float,
    val startWidth: Float,
    val windFactor: Float,
    val hitch: Boolean,
    val brief: StageBrief
)

data class Floor(
    val index: Int,
    val left: Float,
    val width: Float,
    val perfect: Boolean,
    val anchor: Boolean,
    val anchorLocked: Boolean
)

sealed interface PlacementResult {
    data object Miss : PlacementResult

    data class Placed(
        val floor: Floor,
        val perfect: Boolean,
        val anchorLocked: Boolean,
        val sliverLeft: Float,
        val sliverWidth: Float
    ) : PlacementResult
}

data class Checkpoint(
    val stageId: Int,
    val floorIndex: Int,
    val lockedWidth: Float,
    val score: Int
)

data class StageResult(
    val stageId: Int,
    val regionKey: String,
    val mode: GameMode,
    val success: Boolean,
    val floorsPlaced: Int,
    val goalFloors: Int,
    val score: Int,
    val accuracy: Int,
    val finalWidth: Float,
    val startWidth: Float,
    val starDestination: Boolean,
    val starAccuracy: Boolean,
    val starWidth: Boolean,
    val longestStreak: Int,
    val previousBest: Int,
    val newBest: Boolean,
    val checkpointFloor: Int,
    val replayStageId: Int
) {
    val stars: Int
        get() = (if (starDestination) 1 else 0) +
            (if (starAccuracy) 1 else 0) +
            (if (starWidth) 1 else 0)

    val widthPercent: Int
        get() = if (startWidth <= 0f) 0 else ((finalWidth / startWidth) * 100f).toInt()
}

data class StageRecord(
    val stageId: Int,
    val stars: Int,
    val bestScore: Int,
    val bestAccuracy: Int
)

data class DailyEntry(
    val epochDay: Long,
    val score: Int,
    val accuracy: Int
)

data class UnlockItem(
    val key: String,
    val kind: UnlockKind,
    val starCost: Int
)

data class AppSettings(
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val reducedMotion: Boolean = false
)

data class PlayerProfile(
    val totalStars: Int,
    val lastStageId: Int,
    val longestStreak: Int,
    val records: Map<Int, StageRecord>,
    val daily: List<DailyEntry>,
    val settings: AppSettings
)

object ScoreFormat {
    fun format(value: Int): String {
        val digits = value.toString()
        if (digits.length <= 3) {
            return digits
        }
        val builder = StringBuilder()
        var counter = 0
        for (index in digits.length - 1 downTo 0) {
            builder.append(digits[index])
            counter++
            if (counter % 3 == 0 && index > 0) {
                builder.append(' ')
            }
        }
        return builder.reverse().toString()
    }
}

object CalendarClock {
    fun currentEpochDay(): Long = System.currentTimeMillis() / 86_400_000L
}
