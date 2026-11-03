package com.Mombuhu8.Puphur.presentation.game

import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.domain.model.Floor
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageBrief
import com.Mombuhu8.Puphur.domain.model.StageResult

enum class GamePhase {
    READY,
    CARRYING,
    DROPPING,
    SETTLING,
    FINISHED
}

data class GameUiState(
    val stageId: Int = GameConfig.FIRST_STAGE,
    val regionKey: String = "",
    val mode: GameMode = GameMode.CAMPAIGN,
    val goalFloors: Int = 12,
    val startWidth: Float = 180f,
    val lockedWidth: Float = 180f,
    val floors: List<Floor> = emptyList(),
    val carrierWidth: Float = 180f,
    val phase: GamePhase = GamePhase.READY,
    val score: Int = 0,
    val placements: Int = 0,
    val perfects: Int = 0,
    val perfectStreak: Int = 0,
    val longestStreak: Int = 0,
    val showPerfect: Boolean = false,
    val anchorFlash: Boolean = false,
    val failFlash: Boolean = false,
    val hookDim: Boolean = false,
    val brief: StageBrief = StageBrief.NONE,
    val briefVisible: Boolean = false,
    val paused: Boolean = false,
    val checkpointFloor: Int = 0
) {
    val floorsPlaced: Int
        get() = (floors.size - 1).coerceAtLeast(0)

    val currentWidth: Float
        get() = floors.lastOrNull()?.width ?: startWidth

    val widthPercent: Int
        get() = if (startWidth <= 0f) {
            0
        } else {
            ((currentWidth / startWidth) * 100f).toInt().coerceIn(0, 100)
        }

    val goalRatio: Float
        get() = if (goalFloors <= 0) {
            0f
        } else {
            (floorsPlaced.toFloat() / goalFloors).coerceIn(0f, 1f)
        }

    val carrierVisible: Boolean
        get() = phase == GamePhase.READY || phase == GamePhase.CARRYING ||
            phase == GamePhase.DROPPING
}

sealed interface GameEvent {
    data class Finished(val result: StageResult) : GameEvent
}
