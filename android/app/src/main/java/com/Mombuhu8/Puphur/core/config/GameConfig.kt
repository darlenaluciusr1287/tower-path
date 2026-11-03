package com.Mombuhu8.Puphur.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L

    const val TOWER_VIEWPORT_DP = 320f
    const val FLOOR_HEIGHT_DP = 26f
    const val VISIBLE_FLOORS = 9
    const val CARRIER_LANE_DP = 64f
    const val CARRIER_GAP_DP = 18f

    const val PERFECT_TOLERANCE_DP = 2.0f
    const val PERFECT_REWARD_DP = 4.0f
    const val ANCHOR_INTERVAL = 5
    const val ANCHOR_TOLERANCE_DP = 6.0f
    const val MIN_PLAYABLE_WIDTH_DP = 18.0f

    const val MAX_SPEED_DP_S = 420f
    const val SPEED_GROWTH_PER_FLOOR = 7f
    const val WIND_AMPLITUDE_DP = 14f
    const val WIND_PERIOD_MS = 3400f
    const val HITCH_INTERVAL_MS = 2600L
    const val HITCH_DURATION_MS = 220L

    const val TICK_MS = 16L
    const val DROP_ANIM_MS = 140L
    const val SETTLE_MS = 180L
    const val FAILURE_HOLD_MS = 520L
    const val SUCCESS_HOLD_MS = 620L
    const val BRIEF_VISIBLE_MS = 2600L

    const val IDLE_TIMEOUT_MS = 9000L
    const val ROUND_HARD_CAP_MS = 24000L

    const val SCORE_PER_FLOOR = 10
    const val SCORE_PERFECT_BONUS = 25
    const val SCORE_ANCHOR_BONUS = 100
    const val SCORE_STAGE_BONUS_PER_FLOOR = 15
    const val MAX_COMBO = 3.0f
    const val COMBO_STEP = 0.1f

    const val STAR_ACCURACY_PERCENT = 60
    const val STAR_WIDTH_RATIO = 0.70f
    const val STAR_WIDTH_PERCENT = 70

    const val FIRST_STAGE = 1
    const val LAST_STAGE = 15
    const val DAILY_HISTORY_SIZE = 7
    const val DAILY_STAGE_ID = 101
    const val PRACTICE_STAGE_OFFSET = 200
}
