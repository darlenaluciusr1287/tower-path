package com.Mombuhu8.Puphur.domain.usecase

import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.domain.model.CalendarClock
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageBrief
import com.Mombuhu8.Puphur.domain.model.StageSpec
import com.Mombuhu8.Puphur.domain.model.TowerRegions

class BuildStageSpecUseCase {

    operator fun invoke(stageId: Int, mode: GameMode): StageSpec {
        val campaign = campaignSpec(stageId.coerceIn(GameConfig.FIRST_STAGE, GameConfig.LAST_STAGE))
        return when (mode) {
            GameMode.CAMPAIGN -> campaign
            GameMode.PRACTICE -> campaign.copy(
                goalFloors = 12,
                windFactor = 0f,
                hitch = false,
                brief = StageBrief.OVERLAP
            )
            GameMode.DAILY -> dailySpec()
        }
    }

    fun nextStage(stageId: Int): Int =
        (stageId + 1).coerceAtMost(GameConfig.LAST_STAGE)

    private fun dailySpec(): StageSpec {
        val seed = CalendarClock.currentEpochDay()
        val wobble = (seed % 5L).toInt()
        return StageSpec(
            stageId = GameConfig.DAILY_STAGE_ID,
            regionKey = TowerRegions.GLASS,
            goalFloors = 20,
            baseSpeed = 225f + wobble * 5f,
            startWidth = 168f,
            windFactor = 0.40f,
            hitch = false,
            brief = StageBrief.WIND
        )
    }

    private fun campaignSpec(stageId: Int): StageSpec = when (stageId) {
        1 -> StageSpec(1, TowerRegions.HARBOUR, 12, 150f, 180f, 0f, false, StageBrief.OVERLAP)
        2 -> StageSpec(2, TowerRegions.HARBOUR, 14, 165f, 180f, 0f, false, StageBrief.NONE)
        3 -> StageSpec(3, TowerRegions.HARBOUR, 15, 175f, 180f, 0f, false, StageBrief.ANCHOR)
        4 -> StageSpec(4, TowerRegions.HARBOUR, 16, 185f, 180f, 0f, false, StageBrief.NONE)
        5 -> StageSpec(5, TowerRegions.HARBOUR, 18, 200f, 180f, 0f, false, StageBrief.NONE)
        6 -> StageSpec(6, TowerRegions.GLASS, 16, 190f, 168f, 0.25f, false, StageBrief.WIND)
        7 -> StageSpec(7, TowerRegions.GLASS, 18, 205f, 168f, 0.35f, false, StageBrief.NONE)
        8 -> StageSpec(8, TowerRegions.GLASS, 19, 215f, 168f, 0.40f, false, StageBrief.NONE)
        9 -> StageSpec(9, TowerRegions.GLASS, 20, 230f, 168f, 0.45f, false, StageBrief.NONE)
        10 -> StageSpec(10, TowerRegions.GLASS, 22, 245f, 168f, 0.50f, false, StageBrief.NONE)
        11 -> StageSpec(11, TowerRegions.SIGNAL, 20, 240f, 156f, 0f, true, StageBrief.HITCH)
        12 -> StageSpec(12, TowerRegions.SIGNAL, 22, 255f, 156f, 0.30f, true, StageBrief.NONE)
        13 -> StageSpec(13, TowerRegions.SIGNAL, 24, 270f, 156f, 0.35f, true, StageBrief.NONE)
        14 -> StageSpec(14, TowerRegions.SIGNAL, 26, 285f, 156f, 0.45f, true, StageBrief.NONE)
        else -> StageSpec(15, TowerRegions.SIGNAL, 30, 300f, 156f, 0.50f, true, StageBrief.NONE)
    }
}
