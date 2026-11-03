package com.Mombuhu8.Puphur.core.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.Mombuhu8.Puphur.data.local.TowerPreferences
import com.Mombuhu8.Puphur.data.repository.ProgressRepositoryImpl
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.domain.repository.ProgressRepository
import com.Mombuhu8.Puphur.domain.usecase.BuildStageSpecUseCase
import com.Mombuhu8.Puphur.domain.usecase.EvaluateStageUseCase
import com.Mombuhu8.Puphur.domain.usecase.PlacePlatformUseCase

object ServiceLocator {

    private var progress: ProgressRepository? = null
    private val buildStageSpec = BuildStageSpecUseCase()
    private val placePlatform = PlacePlatformUseCase()
    private val evaluateStage = EvaluateStageUseCase()

    fun initialise(context: Context) {
        if (progress == null) {
            progress = ProgressRepositoryImpl(TowerPreferences(context.applicationContext))
        }
    }

    fun factory(
        stageId: Int = 1,
        mode: GameMode = GameMode.CAMPAIGN,
        result: StageResult? = null
    ): ViewModelProvider.Factory = AppViewModelFactory(
        progress = checkNotNull(progress),
        buildStageSpec = buildStageSpec,
        placePlatform = placePlatform,
        evaluateStage = evaluateStage,
        stageId = stageId,
        mode = mode,
        result = result
    )
}
