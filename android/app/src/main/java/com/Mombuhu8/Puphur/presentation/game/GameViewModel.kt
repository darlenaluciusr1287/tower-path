package com.Mombuhu8.Puphur.presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.domain.model.Checkpoint
import com.Mombuhu8.Puphur.domain.model.Floor
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.PlacementResult
import com.Mombuhu8.Puphur.domain.model.StageBrief
import com.Mombuhu8.Puphur.domain.model.StageSpec
import com.Mombuhu8.Puphur.domain.repository.ProgressRepository
import com.Mombuhu8.Puphur.domain.usecase.BuildStageSpecUseCase
import com.Mombuhu8.Puphur.domain.usecase.EvaluateStageUseCase
import com.Mombuhu8.Puphur.domain.usecase.PlacePlatformUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.sin

class GameViewModel(
    private val progress: ProgressRepository,
    private val buildStageSpec: BuildStageSpecUseCase,
    private val placePlatform: PlacePlatformUseCase,
    private val evaluateStage: EvaluateStageUseCase,
    private val stageId: Int,
    private val mode: GameMode
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events: Flow<GameEvent> = _events.receiveAsFlow()

    var carrierOffset by mutableFloatStateOf(0f)
        private set

    private val spec: StageSpec = buildStageSpec(stageId, mode)

    private var loopJob: Job? = null
    private var idleJob: Job? = null
    private var capJob: Job? = null
    private var briefJob: Job? = null
    private var roundOver = false
    private var travel = 0f
    private var direction = 1f
    private var elapsed = 0L

    init {
        openStage()
    }

    fun drop() {
        val current = _state.value
        if (roundOver || current.paused) {
            return
        }
        if (current.phase != GamePhase.READY && current.phase != GamePhase.CARRYING) {
            return
        }
        val landedLeft = carrierOffset
        briefJob?.cancel()
        _state.update { it.copy(phase = GamePhase.DROPPING, briefVisible = false) }
        viewModelScope.launch {
            delay(GameConfig.DROP_ANIM_MS)
            settle(landedLeft)
        }
    }

    fun pause() {
        if (_state.value.paused) {
            return
        }
        _state.update { it.copy(paused = true) }
        stopTimers()
    }

    fun openPause() {
        pause()
    }

    fun resume() {
        if (!_state.value.paused || roundOver) {
            return
        }
        _state.update { it.copy(paused = false) }
        startLoop()
        armIdle()
        armHardCap()
    }

    fun restart() {
        stopTimers()
        roundOver = false
        openStage()
    }

    private fun openStage() {
        val checkpoint = if (mode == GameMode.CAMPAIGN) progress.checkpoint(spec.stageId) else null
        val lockedWidth = checkpoint?.lockedWidth ?: spec.startWidth
        val baseIndex = checkpoint?.floorIndex ?: 0
        val centre = (GameConfig.TOWER_VIEWPORT_DP - lockedWidth) / 2f

        val floors = (0..baseIndex).map { index ->
            Floor(
                index = index,
                left = centre,
                width = lockedWidth,
                perfect = false,
                anchor = index > 0 && index % GameConfig.ANCHOR_INTERVAL == 0,
                anchorLocked = index > 0 && index % GameConfig.ANCHOR_INTERVAL == 0
            )
        }

        travel = 0f
        direction = 1f
        elapsed = 0L
        carrierOffset = 0f

        _state.value = GameUiState(
            stageId = spec.stageId,
            regionKey = spec.regionKey,
            mode = mode,
            goalFloors = spec.goalFloors,
            startWidth = spec.startWidth,
            lockedWidth = lockedWidth,
            floors = floors,
            carrierWidth = lockedWidth,
            phase = GamePhase.CARRYING,
            score = checkpoint?.score ?: 0,
            checkpointFloor = baseIndex,
            brief = spec.brief,
            briefVisible = spec.brief != StageBrief.NONE
        )

        startLoop()
        armIdle()
        armHardCap()
        armBrief()
    }

    private fun armBrief() {
        briefJob?.cancel()
        if (spec.brief == StageBrief.NONE) {
            return
        }
        briefJob = viewModelScope.launch {
            delay(GameConfig.BRIEF_VISIBLE_MS)
            _state.update { it.copy(briefVisible = false) }
        }
    }

    private fun armIdle() {
        idleJob?.cancel()
        idleJob = viewModelScope.launch {
            delay(GameConfig.IDLE_TIMEOUT_MS)
            finishRound(false)
        }
    }

    private fun armHardCap() {
        if (capJob != null) {
            return
        }
        capJob = viewModelScope.launch {
            delay(GameConfig.ROUND_HARD_CAP_MS)
            finishRound(false)
        }
    }

    private fun startLoop() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            while (isActive) {
                delay(GameConfig.TICK_MS)
                val current = _state.value
                if (current.paused || roundOver) {
                    continue
                }
                if (current.phase != GamePhase.CARRYING && current.phase != GamePhase.READY) {
                    continue
                }
                elapsed += GameConfig.TICK_MS
                advance(current)
            }
        }
    }

    private fun advance(current: GameUiState) {
        val span = GameConfig.TOWER_VIEWPORT_DP - current.carrierWidth
        if (span <= 0f) {
            carrierOffset = 0f
            return
        }
        val hitching = spec.hitch &&
            (elapsed % GameConfig.HITCH_INTERVAL_MS) < GameConfig.HITCH_DURATION_MS
        val telegraph = spec.hitch &&
            (elapsed % GameConfig.HITCH_INTERVAL_MS) >= GameConfig.HITCH_INTERVAL_MS - 120L

        if (telegraph != current.hookDim) {
            _state.update { it.copy(hookDim = telegraph) }
        }

        if (!hitching) {
            val speed = min(
                GameConfig.MAX_SPEED_DP_S,
                spec.baseSpeed + GameConfig.SPEED_GROWTH_PER_FLOOR * current.floorsPlaced
            )
            travel += direction * speed * GameConfig.TICK_MS / 1000f
            if (travel >= span) {
                travel = span
                direction = -1f
            } else if (travel <= 0f) {
                travel = 0f
                direction = 1f
            }
        }

        val drift = if (spec.windFactor <= 0f) {
            0f
        } else {
            GameConfig.WIND_AMPLITUDE_DP * spec.windFactor *
                sin(2.0 * PI * elapsed / GameConfig.WIND_PERIOD_MS).toFloat()
        }
        carrierOffset = (travel + drift).coerceIn(0f, span)
    }

    private fun settle(landedLeft: Float) {
        val current = _state.value
        val top = current.floors.lastOrNull() ?: return
        when (
            val outcome = placePlatform(
                top = top,
                carrierLeft = landedLeft,
                carrierWidth = current.carrierWidth,
                lockedWidth = current.lockedWidth
            )
        ) {
            is PlacementResult.Miss -> {
                _state.update { it.copy(failFlash = true) }
                finishRound(false)
            }

            is PlacementResult.Placed -> applyPlacement(current, outcome)
        }
    }

    private fun applyPlacement(current: GameUiState, outcome: PlacementResult.Placed) {
        val streak = if (outcome.perfect) current.perfectStreak + 1 else 0
        val combo = min(GameConfig.MAX_COMBO, 1f + GameConfig.COMBO_STEP * streak)
        var gained = (GameConfig.SCORE_PER_FLOOR * combo).toInt()
        if (outcome.perfect) {
            gained += GameConfig.SCORE_PERFECT_BONUS
        }
        if (outcome.anchorLocked) {
            gained += GameConfig.SCORE_ANCHOR_BONUS
        }

        val nextLocked = if (outcome.anchorLocked) outcome.floor.width else current.lockedWidth
        val floors = current.floors + outcome.floor
        val placed = floors.size - 1
        val score = current.score + gained

        _state.update {
            it.copy(
                floors = floors,
                lockedWidth = nextLocked,
                carrierWidth = outcome.floor.width,
                phase = GamePhase.SETTLING,
                score = score,
                placements = it.placements + 1,
                perfects = if (outcome.perfect) it.perfects + 1 else it.perfects,
                perfectStreak = streak,
                longestStreak = maxOf(it.longestStreak, streak),
                showPerfect = outcome.perfect,
                anchorFlash = outcome.anchorLocked,
                checkpointFloor = if (outcome.anchorLocked) {
                    outcome.floor.index
                } else {
                    it.checkpointFloor
                }
            )
        }

        if (outcome.anchorLocked && mode == GameMode.CAMPAIGN) {
            progress.storeCheckpoint(
                Checkpoint(
                    stageId = spec.stageId,
                    floorIndex = outcome.floor.index,
                    lockedWidth = outcome.floor.width,
                    score = score
                )
            )
        }

        travel = 0f
        direction = 1f
        carrierOffset = 0f

        if (placed >= spec.goalFloors) {
            finishRound(true)
            return
        }

        armIdle()
        viewModelScope.launch {
            delay(GameConfig.SETTLE_MS)
            if (!roundOver) {
                _state.update {
                    it.copy(phase = GamePhase.CARRYING, showPerfect = false, anchorFlash = false)
                }
            }
        }
    }

    private fun finishRound(success: Boolean) {
        if (roundOver) {
            return
        }
        roundOver = true
        stopTimers()
        val current = _state.value
        _state.update { it.copy(phase = GamePhase.FINISHED, failFlash = !success) }

        val result = evaluateStage(
            spec = spec,
            mode = mode,
            success = success,
            floorsPlaced = current.floorsPlaced,
            placements = current.placements,
            perfects = current.perfects,
            rawScore = current.score,
            finalWidth = current.currentWidth,
            longestStreak = current.longestStreak,
            previousBest = progress.bestScore(spec.stageId),
            checkpointFloor = current.checkpointFloor,
            nextStageId = buildStageSpec.nextStage(spec.stageId)
        )
        progress.persistResult(result)

        viewModelScope.launch {
            delay(if (success) GameConfig.SUCCESS_HOLD_MS else GameConfig.FAILURE_HOLD_MS)
            _events.send(GameEvent.Finished(result))
        }
    }

    private fun stopTimers() {
        loopJob?.cancel()
        idleJob?.cancel()
        capJob?.cancel()
        briefJob?.cancel()
        loopJob = null
        idleJob = null
        capJob = null
        briefJob = null
    }

    override fun onCleared() {
        stopTimers()
        super.onCleared()
    }
}
