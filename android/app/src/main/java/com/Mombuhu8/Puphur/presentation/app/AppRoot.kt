package com.Mombuhu8.Puphur.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Mombuhu8.Puphur.core.di.ServiceLocator
import com.Mombuhu8.Puphur.core.navigation.Screen
import com.Mombuhu8.Puphur.core.theme.BgNight
import com.Mombuhu8.Puphur.presentation.game.GameRoute
import com.Mombuhu8.Puphur.presentation.gameover.GameOverRoute
import com.Mombuhu8.Puphur.presentation.menu.MenuRoute
import com.Mombuhu8.Puphur.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.factory())
    val screen by appViewModel.screen.collectAsStateWithLifecycle()

    BackHandler(enabled = screen !is Screen.Menu) {
        if (screen !is Screen.Splash) {
            appViewModel.back()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgNight)
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                when (targetState) {
                    is Screen.Game -> (
                        slideInVertically(
                            animationSpec = tween(320),
                            initialOffsetY = { it / 4 }
                        ) + fadeIn(tween(320))
                        ) togetherWith fadeOut(tween(200))

                    is Screen.GameOver -> (
                        fadeIn(tween(260)) + scaleIn(initialScale = 0.94f)
                        ) togetherWith fadeOut(tween(200))

                    else -> fadeIn(tween(300)) togetherWith fadeOut(tween(220))
                }
            },
            label = "screen"
        ) { current ->
            when (current) {
                is Screen.Splash -> SplashRoute(onFinished = { appViewModel.openMenu() })

                is Screen.Menu -> MenuRoute(
                    onPlay = { stageId, mode ->
                        appViewModel.navigate(Screen.Game(stageId, mode))
                    }
                )

                is Screen.Game -> GameRoute(
                    stageId = current.stageId,
                    mode = current.mode,
                    onFinished = { result ->
                        appViewModel.navigateReplacing(Screen.GameOver(result))
                    },
                    onMenu = { appViewModel.openMenu() }
                )

                is Screen.GameOver -> GameOverRoute(
                    result = current.result,
                    onPlayAgain = { stageId, mode ->
                        appViewModel.navigateReplacing(Screen.Game(stageId, mode))
                    },
                    onMenu = { appViewModel.openMenu() }
                )
            }
        }
    }
}
