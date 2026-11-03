package com.Mombuhu8.Puphur.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.domain.model.AppSettings
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.repository.ProgressRepository
import com.Mombuhu8.Puphur.domain.usecase.BuildStageSpecUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progress: ProgressRepository,
    private val buildStageSpec: BuildStageSpecUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch { reload() }
    }

    fun refresh() {
        viewModelScope.launch { reload() }
    }

    fun selectMode(mode: GameMode) {
        _state.update { it.copy(selectedMode = mode) }
    }

    fun selectStage(stageId: Int) {
        viewModelScope.launch {
            progress.selectStage(stageId)
            reload()
            _state.update { it.copy(panel = MenuPanel.NONE) }
        }
    }

    fun openPanel(panel: MenuPanel) {
        _state.update { it.copy(panel = panel, confirmingReset = false) }
    }

    fun closePanel() {
        _state.update { it.copy(panel = MenuPanel.NONE, confirmingReset = false) }
    }

    fun toggleSound() {
        val next = _state.value.settings.let { it.copy(soundEnabled = !it.soundEnabled) }
        applySettings(next)
    }

    fun toggleHaptics() {
        val next = _state.value.settings.let { it.copy(hapticsEnabled = !it.hapticsEnabled) }
        applySettings(next)
    }

    fun toggleReducedMotion() {
        val next = _state.value.settings.let { it.copy(reducedMotion = !it.reducedMotion) }
        applySettings(next)
    }

    fun requestReset() {
        _state.update { it.copy(confirmingReset = true) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            progress.resetProgress()
            reload()
            _state.update { it.copy(confirmingReset = false) }
        }
    }

    private fun applySettings(settings: AppSettings) {
        viewModelScope.launch {
            progress.updateSettings(settings)
            _state.update { it.copy(settings = settings) }
        }
    }

    private fun reload() {
        val profile = progress.profile()
        val unlockedKeys = progress.unlockCatalogue()
            .filter { it.starCost <= profile.totalStars }
            .map { it.key }
            .toSet()

        val stages = (GameConfig.FIRST_STAGE..GameConfig.LAST_STAGE).map { stageId ->
            val spec = buildStageSpec(stageId, GameMode.CAMPAIGN)
            StageEntry(
                stageId = stageId,
                regionKey = spec.regionKey,
                goalFloors = spec.goalFloors,
                stars = profile.records[stageId]?.stars ?: 0,
                unlocked = stageId <= profile.lastStageId
            )
        }

        val unlocks = progress.unlockCatalogue().map { item ->
            UnlockEntry(
                key = item.key,
                kind = item.kind,
                starCost = item.starCost,
                unlocked = unlockedKeys.contains(item.key)
            )
        }

        val regionsCleared = progress.regions().count { region ->
            (region.firstStage..region.lastStage).all { stageId ->
                (profile.records[stageId]?.stars ?: 0) > 0
            }
        }

        val nextSpec = buildStageSpec(profile.lastStageId, GameMode.CAMPAIGN)

        _state.update { current ->
            current.copy(
                loading = false,
                totalStars = profile.totalStars,
                nextStageId = profile.lastStageId,
                nextStageRegionKey = nextSpec.regionKey,
                nextStageGoal = nextSpec.goalFloors,
                nextStageStars = profile.records[profile.lastStageId]?.stars ?: 0,
                stages = stages,
                unlocks = unlocks,
                records = RecordsSummary(
                    regionsCleared = regionsCleared,
                    bestScore = profile.records.values.maxOfOrNull { it.bestScore } ?: 0,
                    bestAccuracy = profile.records.values.maxOfOrNull { it.bestAccuracy } ?: 0,
                    longestStreak = profile.longestStreak,
                    daily = profile.daily
                ),
                settings = profile.settings
            )
        }
    }
}
