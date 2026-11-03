package com.Mombuhu8.Puphur.data.local

import android.content.Context
import android.content.SharedPreferences

class TowerPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    var lastStageId: Int
        get() = prefs.getInt(KEY_LAST_STAGE, 1)
        set(value) = prefs.edit().putInt(KEY_LAST_STAGE, value).apply()

    var longestStreak: Int
        get() = prefs.getInt(KEY_STREAK, 0)
        set(value) = prefs.edit().putInt(KEY_STREAK, value).apply()

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND, value).apply()

    var hapticsEnabled: Boolean
        get() = prefs.getBoolean(KEY_HAPTICS, true)
        set(value) = prefs.edit().putBoolean(KEY_HAPTICS, value).apply()

    var reducedMotion: Boolean
        get() = prefs.getBoolean(KEY_MOTION, false)
        set(value) = prefs.edit().putBoolean(KEY_MOTION, value).apply()

    var dailyHistory: String
        get() = prefs.getString(KEY_DAILY, "").orEmpty()
        set(value) = prefs.edit().putString(KEY_DAILY, value).apply()

    var seeded: Boolean
        get() = prefs.getBoolean(KEY_SEEDED, false)
        set(value) = prefs.edit().putBoolean(KEY_SEEDED, value).apply()

    fun stars(stageId: Int): Int = prefs.getInt("$KEY_STARS$stageId", 0)

    fun bestScore(stageId: Int): Int = prefs.getInt("$KEY_BEST$stageId", 0)

    fun bestAccuracy(stageId: Int): Int = prefs.getInt("$KEY_ACC$stageId", 0)

    fun writeStageRecord(stageId: Int, stars: Int, bestScore: Int, bestAccuracy: Int) {
        prefs.edit()
            .putInt("$KEY_STARS$stageId", stars)
            .putInt("$KEY_BEST$stageId", bestScore)
            .putInt("$KEY_ACC$stageId", bestAccuracy)
            .apply()
    }

    fun unlockedKeys(): Set<String> = prefs.getStringSet(KEY_UNLOCKED, emptySet()).orEmpty()

    fun writeUnlockedKeys(keys: Set<String>) {
        prefs.edit().putStringSet(KEY_UNLOCKED, keys).apply()
    }

    fun checkpoint(stageId: Int): String = prefs.getString("$KEY_CHECKPOINT$stageId", "").orEmpty()

    fun writeCheckpoint(stageId: Int, raw: String) {
        prefs.edit().putString("$KEY_CHECKPOINT$stageId", raw).apply()
    }

    fun removeCheckpoint(stageId: Int) {
        prefs.edit().remove("$KEY_CHECKPOINT$stageId").apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val STORE = "towerpath_progress"
        const val KEY_LAST_STAGE = "last_stage"
        const val KEY_STREAK = "longest_streak"
        const val KEY_SOUND = "sound_enabled"
        const val KEY_HAPTICS = "haptics_enabled"
        const val KEY_MOTION = "reduced_motion"
        const val KEY_DAILY = "daily_history"
        const val KEY_SEEDED = "history_seeded"
        const val KEY_STARS = "stage_stars_"
        const val KEY_BEST = "stage_best_"
        const val KEY_ACC = "stage_acc_"
        const val KEY_UNLOCKED = "unlocked_keys"
        const val KEY_CHECKPOINT = "checkpoint_"
    }
}
