package com.Mombuhu8.Puphur.presentation.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.di.ServiceLocator
import com.Mombuhu8.Puphur.core.theme.DangerRed
import com.Mombuhu8.Puphur.core.theme.DimText
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.GameOverlay
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.StrokeGlass
import com.Mombuhu8.Puphur.core.theme.SurfaceDeepBlue
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.TowerBlue
import com.Mombuhu8.Puphur.core.theme.WarmOrange
import com.Mombuhu8.Puphur.core.ui.PrimaryButton
import com.Mombuhu8.Puphur.core.ui.ScreenBackground
import com.Mombuhu8.Puphur.core.ui.modeLabel
import com.Mombuhu8.Puphur.core.ui.regionLabel
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.ScoreFormat
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.presentation.app.ScreenScope
import com.Mombuhu8.Puphur.presentation.dialog.PauseDialog

@Composable
fun GameRoute(
    stageId: Int,
    mode: GameMode,
    onFinished: (StageResult) -> Unit,
    onMenu: () -> Unit
) {
    ScreenScope(key = "game") {
        GameHost(
            stageId = stageId,
            mode = mode,
            onFinished = onFinished,
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameHost(
    stageId: Int,
    mode: GameMode,
    onFinished: (StageResult) -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(
        factory = ServiceLocator.factory(stageId = stageId, mode = mode)
    )
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.result)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameScreen(
        state = state,
        carrierX = { gameViewModel.carrierOffset },
        onDrop = gameViewModel::drop,
        onPause = gameViewModel::openPause,
        onResume = gameViewModel::resume,
        onRestart = gameViewModel::restart,
        onMenu = onMenu
    )
}

@Composable
private fun GameScreen(
    state: GameUiState,
    carrierX: () -> Float,
    onDrop: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    val boardInteraction = remember { MutableInteractionSource() }

    ScreenBackground(overlay = GameOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state)
            WidthMeter(state = state)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        interactionSource = boardInteraction,
                        indication = null,
                        onClick = onDrop
                    )
            ) {
                GoalRail(
                    ratio = state.goalRatio,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )

                TowerColumn(
                    floors = state.floors,
                    carrierWidth = state.carrierWidth,
                    carrierVisible = state.carrierVisible,
                    hookDim = state.hookDim,
                    failFlash = state.failFlash,
                    carrierX = carrierX,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 128.dp)
                )

                val perfectAlpha by animateFloatAsState(
                    targetValue = if (state.showPerfect) 1f else 0f,
                    animationSpec = tween(260)
                )
                val perfectLift by animateFloatAsState(
                    targetValue = if (state.showPerfect) 0f else 20f,
                    animationSpec = tween(360)
                )
                Text(
                    text = stringResource(R.string.perfect),
                    style = MaterialTheme.typography.titleMedium,
                    color = ElectricCyan,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 24.dp)
                        .graphicsLayer {
                            alpha = perfectAlpha
                            translationY = perfectLift.dp.toPx()
                        }
                )

                StageBriefPanel(
                    brief = state.brief,
                    visible = state.briefVisible,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 24.dp, end = 24.dp, bottom = 120.dp)
                )

                ControlBar(
                    state = state,
                    onDrop = onDrop,
                    onPause = onPause,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 28.dp)
                )
            }
        }

        if (state.paused) {
            PauseDialog(
                onResume = onResume,
                onRestart = onRestart,
                onMenu = onMenu
            )
        }
    }
}

@Composable
private fun GameHeader(state: GameUiState) {
    val counterDescription = stringResource(
        R.string.cd_floor_counter,
        state.floorsPlaced,
        state.goalFloors
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0x660A1628))
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.width(104.dp)) {
            Text(
                text = stringResource(R.string.stage_label, state.stageId),
                style = MaterialTheme.typography.labelSmall,
                color = DimText,
                maxLines = 1
            )
            if (state.mode == GameMode.CAMPAIGN) {
                Text(
                    text = regionLabel(state.regionKey),
                    style = MaterialTheme.typography.titleMedium,
                    color = LightText,
                    maxLines = 1
                )
            } else {
                Text(
                    text = modeLabel(state.mode),
                    style = MaterialTheme.typography.titleMedium,
                    color = WarmOrange,
                    maxLines = 1
                )
            }
        }

        Text(
            text = stringResource(
                R.string.floor_counter,
                state.floorsPlaced,
                state.goalFloors
            ),
            style = MaterialTheme.typography.headlineMedium,
            fontFamily = FontFamily.Monospace,
            color = LightText,
            modifier = Modifier.semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = counterDescription
            }
        )

        Text(
            text = ScoreFormat.format(state.score),
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Monospace,
            color = ElectricCyan,
            maxLines = 1,
            modifier = Modifier.width(104.dp)
        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(StrokeGlass)
    )
}

@Composable
private fun WidthMeter(state: GameUiState) {
    val ratio by animateFloatAsState(
        targetValue = (state.widthPercent / 100f).coerceIn(0f, 1f),
        animationSpec = tween(300)
    )
    val description = stringResource(R.string.sd_width, state.widthPercent)
    val accent = when {
        state.widthPercent >= 70 -> ElectricCyan
        state.widthPercent >= 40 -> TowerBlue
        else -> DangerRed
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp)
            .height(4.dp)
            .background(SurfaceRaised, RoundedCornerShape(2.dp))
            .semantics { stateDescription = description }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(ratio)
                .height(4.dp)
                .background(accent, RoundedCornerShape(2.dp))
        )
    }
}

@Composable
private fun GoalRail(ratio: Float, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(end = 8.dp)
            .fillMaxHeight(0.62f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_5),
            contentDescription = stringResource(R.string.cd_destination),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(6.dp)
                .weight(1f)
                .background(SurfaceRaised, RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .fillMaxHeight(ratio.coerceIn(0f, 1f))
                    .background(
                        Brush.verticalGradient(listOf(WarmOrange, ElectricCyan)),
                        RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

@Composable
private fun ControlBar(
    state: GameUiState,
    onDrop: () -> Unit,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(26.dp)
    val pauseLabel = stringResource(R.string.cd_pause)
    Row(
        modifier = modifier
            .width(316.dp)
            .height(72.dp)
            .shadow(20.dp, shape, ambientColor = TowerBlue, spotColor = TowerBlue)
            .background(SurfaceDeepBlue.copy(alpha = 0.88f), shape)
            .border(1.dp, StrokeGlass, shape)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(SurfaceRaised.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                .border(1.dp, StrokeGlass, RoundedCornerShape(16.dp))
                .clickable(onClick = onPause)
                .semantics { contentDescription = pauseLabel },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }

        PrimaryButton(
            label = stringResource(R.string.drop),
            onClick = onDrop,
            enabled = state.phase != GamePhase.FINISHED,
            height = 56.dp,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .width(58.dp)
                .height(48.dp)
                .background(SurfaceRaised.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                .border(1.dp, StrokeGlass, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.percent_value, state.widthPercent),
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                color = ElectricCyan,
                maxLines = 1
            )
        }
    }
}
