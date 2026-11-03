package com.Mombuhu8.Puphur.domain.repository

import com.Mombuhu8.Puphur.domain.model.AppSettings
import com.Mombuhu8.Puphur.domain.model.Checkpoint
import com.Mombuhu8.Puphur.domain.model.PlayerProfile
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.domain.model.TowerRegion
import com.Mombuhu8.Puphur.domain.model.UnlockItem

interface ProgressRepository {
    fun regions(): List<TowerRegion>

    fun unlockCatalogue(): List<UnlockItem>

    fun unlockedKeys(): Set<String>

    fun profile(): PlayerProfile

    fun bestScore(stageId: Int): Int

    fun persistResult(result: StageResult)

    fun selectStage(stageId: Int)

    fun checkpoint(stageId: Int): Checkpoint?

    fun storeCheckpoint(checkpoint: Checkpoint)

    fun clearCheckpoint(stageId: Int)

    fun updateSettings(settings: AppSettings)

    fun resetProgress()
}
