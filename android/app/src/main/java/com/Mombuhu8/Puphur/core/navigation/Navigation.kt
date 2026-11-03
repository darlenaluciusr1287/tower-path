package com.Mombuhu8.Puphur.core.navigation

import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageResult

sealed interface Screen {
    data object Splash : Screen

    data object Menu : Screen

    data class Game(val stageId: Int, val mode: GameMode) : Screen

    data class GameOver(val result: StageResult) : Screen
}
