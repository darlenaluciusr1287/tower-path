package com.Mombuhu8.Puphur.domain.usecase

import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.domain.model.StageSpec
import kotlin.math.roundToInt

class EvaluateStageUseCase {

    operator fun invoke(
        spec: StageSpec,
        mode: GameMode,
        success: Boolean,
        floorsPlaced: Int,
        placements: Int,
        perfects: Int,
        rawScore: Int,
        finalWidth: Float,
        longestStreak: Int,
        previousBest: Int,
        checkpointFloor: Int,
        nextStageId: Int
    ): StageResult {
        val accuracy = if (placements <= 0) {
            0
        } else {
            (perfects * 100f / placements).roundToInt()
        }
        val bonus = if (success) spec.goalFloors * GameConfig.SCORE_STAGE_BONUS_PER_FLOOR else 0
        val score = rawScore + bonus

        val starDestination = floorsPlaced >= spec.goalFloors
        val starAccuracy = accuracy >= GameConfig.STAR_ACCURACY_PERCENT
        val starWidth = finalWidth >= GameConfig.STAR_WIDTH_RATIO * spec.startWidth

        return StageResult(
            stageId = spec.stageId,
            regionKey = spec.regionKey,
            mode = mode,
            success = success,
            floorsPlaced = floorsPlaced,
            goalFloors = spec.goalFloors,
            score = score,
            accuracy = accuracy,
            finalWidth = finalWidth,
            startWidth = spec.startWidth,
            starDestination = starDestination,
            starAccuracy = starAccuracy,
            starWidth = starWidth,
            longestStreak = longestStreak,
            previousBest = previousBest,
            newBest = score > previousBest,
            checkpointFloor = checkpointFloor,
            replayStageId = if (success) nextStageId else spec.stageId
        )
    }
}
