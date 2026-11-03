package com.Mombuhu8.Puphur.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.domain.repository.ProgressRepository
import com.Mombuhu8.Puphur.domain.usecase.BuildStageSpecUseCase
import com.Mombuhu8.Puphur.domain.usecase.EvaluateStageUseCase
import com.Mombuhu8.Puphur.domain.usecase.PlacePlatformUseCase
import com.Mombuhu8.Puphur.presentation.app.AppViewModel
import com.Mombuhu8.Puphur.presentation.game.GameViewModel
import com.Mombuhu8.Puphur.presentation.gameover.GameOverViewModel
import com.Mombuhu8.Puphur.presentation.menu.MenuViewModel
import com.Mombuhu8.Puphur.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val progress: ProgressRepository,
    private val buildStageSpec: BuildStageSpecUseCase,
    private val placePlatform: PlacePlatformUseCase,
    private val evaluateStage: EvaluateStageUseCase,
    private val stageId: Int,
    private val mode: GameMode,
    private val result: StageResult?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AppViewModel::class.java) ->
            AppViewModel() as T

        modelClass.isAssignableFrom(SplashViewModel::class.java) ->
            SplashViewModel() as T

        modelClass.isAssignableFrom(MenuViewModel::class.java) ->
            MenuViewModel(progress, buildStageSpec) as T

        modelClass.isAssignableFrom(GameViewModel::class.java) ->
            GameViewModel(
                progress,
                buildStageSpec,
                placePlatform,
                evaluateStage,
                stageId,
                mode
            ) as T

        modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
            GameOverViewModel(progress, checkNotNull(result)) as T

        else -> throw IllegalArgumentException(modelClass.name)
    }
}
