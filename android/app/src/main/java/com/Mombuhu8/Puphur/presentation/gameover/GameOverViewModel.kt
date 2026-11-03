package com.Mombuhu8.Puphur.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameOverUiState(
    val result: StageResult,
    val bestScore: Int = 0,
    val totalStars: Int = 0,
    val hasCheckpoint: Boolean = false
)

class GameOverViewModel(
    private val progress: ProgressRepository,
    result: StageResult
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState(result = result))
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = progress.profile()
            val checkpoint = if (result.mode == GameMode.CAMPAIGN) {
                progress.checkpoint(result.stageId)
            } else {
                null
            }
            _state.update {
                it.copy(
                    bestScore = progress.bestScore(result.stageId),
                    totalStars = profile.totalStars,
                    hasCheckpoint = checkpoint != null
                )
            }
        }
    }
}
