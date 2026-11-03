package com.Mombuhu8.Puphur.data.repository

import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.data.local.TowerPreferences
import com.Mombuhu8.Puphur.data.sample.SampleData
import com.Mombuhu8.Puphur.domain.model.AppSettings
import com.Mombuhu8.Puphur.domain.model.CalendarClock
import com.Mombuhu8.Puphur.domain.model.Checkpoint
import com.Mombuhu8.Puphur.domain.model.DailyEntry
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.PlayerProfile
import com.Mombuhu8.Puphur.domain.model.StageRecord
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.domain.model.TowerRegion
import com.Mombuhu8.Puphur.domain.model.UnlockItem
import com.Mombuhu8.Puphur.domain.repository.ProgressRepository
import kotlin.math.max

class ProgressRepositoryImpl(
    private val preferences: TowerPreferences
) : ProgressRepository {

    init {
        if (!preferences.seeded) {
            preferences.seeded = true
            preferences.dailyHistory = encodeDaily(
                SampleData.seedDailyHistory(CalendarClock.currentEpochDay())
            )
        }
    }

    override fun regions(): List<TowerRegion> = SampleData.regions

    override fun unlockCatalogue(): List<UnlockItem> = SampleData.unlocks

    override fun profile(): PlayerProfile {
        val records = buildMap {
            for (stageId in GameConfig.FIRST_STAGE..GameConfig.LAST_STAGE) {
                put(
                    stageId,
                    StageRecord(
                        stageId = stageId,
                        stars = preferences.stars(stageId),
                        bestScore = preferences.bestScore(stageId),
                        bestAccuracy = preferences.bestAccuracy(stageId)
                    )
                )
            }
        }
        val totalStars = records.values.sumOf { it.stars }
        return PlayerProfile(
            totalStars = totalStars,
            lastStageId = preferences.lastStageId.coerceIn(
                GameConfig.FIRST_STAGE,
                GameConfig.LAST_STAGE
            ),
            longestStreak = preferences.longestStreak,
            records = records,
            daily = decodeDaily(preferences.dailyHistory),
            settings = AppSettings(
                soundEnabled = preferences.soundEnabled,
                hapticsEnabled = preferences.hapticsEnabled,
                reducedMotion = preferences.reducedMotion
            )
        )
    }

    override fun bestScore(stageId: Int): Int = preferences.bestScore(stageId)

    override fun persistResult(result: StageResult) {
        if (result.mode == GameMode.PRACTICE) {
            return
        }
        preferences.longestStreak = max(preferences.longestStreak, result.longestStreak)

        if (result.mode == GameMode.DAILY) {
            storeDaily(result)
            return
        }

        val stars = max(preferences.stars(result.stageId), result.stars)
        val best = max(preferences.bestScore(result.stageId), result.score)
        val accuracy = max(preferences.bestAccuracy(result.stageId), result.accuracy)
        preferences.writeStageRecord(result.stageId, stars, best, accuracy)

        if (result.success) {
            preferences.lastStageId = result.replayStageId
            preferences.removeCheckpoint(result.stageId)
        }
        refreshUnlocks()
    }

    override fun selectStage(stageId: Int) {
        preferences.lastStageId = stageId.coerceIn(GameConfig.FIRST_STAGE, GameConfig.LAST_STAGE)
    }

    override fun checkpoint(stageId: Int): Checkpoint? {
        val raw = preferences.checkpoint(stageId)
        if (raw.isEmpty()) {
            return null
        }
        val parts = raw.split(':')
        if (parts.size != 3) {
            return null
        }
        val floorIndex = parts[0].toIntOrNull() ?: return null
        val width = parts[1].toFloatOrNull() ?: return null
        val score = parts[2].toIntOrNull() ?: return null
        return Checkpoint(stageId, floorIndex, width, score)
    }

    override fun storeCheckpoint(checkpoint: Checkpoint) {
        preferences.writeCheckpoint(
            checkpoint.stageId,
            "${checkpoint.floorIndex}:${checkpoint.lockedWidth}:${checkpoint.score}"
        )
    }

    override fun clearCheckpoint(stageId: Int) {
        preferences.removeCheckpoint(stageId)
    }

    override fun updateSettings(settings: AppSettings) {
        preferences.soundEnabled = settings.soundEnabled
        preferences.hapticsEnabled = settings.hapticsEnabled
        preferences.reducedMotion = settings.reducedMotion
    }

    override fun resetProgress() {
        preferences.clearAll()
        preferences.seeded = true
        preferences.dailyHistory = encodeDaily(
            SampleData.seedDailyHistory(CalendarClock.currentEpochDay())
        )
    }

    override fun unlockedKeys(): Set<String> = preferences.unlockedKeys()

    private fun refreshUnlocks() {
        val total = profile().totalStars
        val keys = SampleData.unlocks.filter { it.starCost <= total }.map { it.key }.toSet()
        preferences.writeUnlockedKeys(keys)
    }

    private fun storeDaily(result: StageResult) {
        val day = CalendarClock.currentEpochDay()
        val existing = decodeDaily(preferences.dailyHistory).toMutableList()
        val previous = existing.firstOrNull { it.epochDay == day }
        existing.removeAll { it.epochDay == day }
        val merged = DailyEntry(
            epochDay = day,
            score = max(previous?.score ?: 0, result.score),
            accuracy = max(previous?.accuracy ?: 0, result.accuracy)
        )
        existing.add(0, merged)
        val trimmed = existing.sortedByDescending { it.epochDay }
            .take(GameConfig.DAILY_HISTORY_SIZE)
        preferences.dailyHistory = encodeDaily(trimmed)
    }

    private fun encodeDaily(entries: List<DailyEntry>): String =
        entries.joinToString(";") { "${it.epochDay}:${it.score}:${it.accuracy}" }

    private fun decodeDaily(raw: String): List<DailyEntry> {
        if (raw.isEmpty()) {
            return emptyList()
        }
        return raw.split(';').mapNotNull { chunk ->
            val parts = chunk.split(':')
            if (parts.size != 3) {
                return@mapNotNull null
            }
            val day = parts[0].toLongOrNull() ?: return@mapNotNull null
            val score = parts[1].toIntOrNull() ?: return@mapNotNull null
            val accuracy = parts[2].toIntOrNull() ?: return@mapNotNull null
            DailyEntry(day, score, accuracy)
        }
    }
}
