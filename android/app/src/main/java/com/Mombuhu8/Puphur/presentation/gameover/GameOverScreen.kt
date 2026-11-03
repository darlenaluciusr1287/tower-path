package com.Mombuhu8.Puphur.presentation.gameover

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.di.ServiceLocator
import com.Mombuhu8.Puphur.core.theme.DangerRed
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.MutedText
import com.Mombuhu8.Puphur.core.theme.ResultOverlay
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.TowerBlue
import com.Mombuhu8.Puphur.core.theme.WarmOrange
import com.Mombuhu8.Puphur.core.ui.GlassCard
import com.Mombuhu8.Puphur.core.ui.PrimaryButton
import com.Mombuhu8.Puphur.core.ui.ScreenBackground
import com.Mombuhu8.Puphur.core.ui.SecondaryButton
import com.Mombuhu8.Puphur.core.ui.StarRow
import com.Mombuhu8.Puphur.core.ui.StatCard
import com.Mombuhu8.Puphur.core.ui.regionLabel
import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.ScoreFormat
import com.Mombuhu8.Puphur.domain.model.StageResult
import com.Mombuhu8.Puphur.presentation.app.ScreenScope

@Composable
fun GameOverRoute(
    result: StageResult,
    onPlayAgain: (Int, GameMode) -> Unit,
    onMenu: () -> Unit
) {
    ScreenScope(key = "gameover") {
        GameOverHost(result = result, onPlayAgain = onPlayAgain, onMenu = onMenu)
    }
}

@Composable
private fun GameOverHost(
    result: StageResult,
    onPlayAgain: (Int, GameMode) -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(
        factory = ServiceLocator.factory(
            stageId = result.stageId,
            mode = result.mode,
            result = result
        )
    )
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    GameOverScreen(
        state = state,
        onPlayAgain = { onPlayAgain(state.result.replayStageId, state.result.mode) },
        onMenu = onMenu
    )
}

@Composable
private fun GameOverScreen(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val result = state.result
    val accent = if (result.success) WarmOrange else DangerRed
    val animatedScore by animateIntAsState(
        targetValue = result.score,
        animationSpec = tween(700)
    )

    ScreenBackground(
        overlay = ResultOverlay,
        accent = Brush.radialGradient(
            colors = listOf(accent.copy(alpha = 0.18f), Color.Transparent),
            radius = 620f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(
                    if (result.success) R.string.route_complete else R.string.route_lost
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = if (result.success) ElectricCyan else DangerRed,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(
                    R.string.stage_line,
                    result.stageId,
                    regionLabel(result.regionKey)
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))
            StarRow(
                earned = result.stars,
                total = 3,
                size = 44.dp,
                gap = 14.dp,
                animated = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StarCondition(label = stringResource(R.string.star_destination))
                StarCondition(
                    label = stringResource(
                        R.string.star_accuracy,
                        GameConfig.STAR_ACCURACY_PERCENT
                    )
                )
                StarCondition(
                    label = stringResource(
                        R.string.star_width,
                        GameConfig.STAR_WIDTH_PERCENT
                    )
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = ScoreFormat.format(animatedScore),
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Monospace,
                color = LightText
            )
            if (result.newBest) {
                Text(
                    text = stringResource(R.string.new_best),
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricCyan
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    value = stringResource(
                        R.string.floor_counter,
                        result.floorsPlaced,
                        result.goalFloors
                    ),
                    label = stringResource(R.string.floors_label),
                    accent = TowerBlue,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = stringResource(R.string.percent_value, result.accuracy),
                    label = stringResource(R.string.accuracy_label),
                    accent = ElectricCyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = ScoreFormat.format(maxOf(state.bestScore, result.score)),
                    label = stringResource(R.string.best_label),
                    accent = WarmOrange,
                    modifier = Modifier.weight(1f)
                )
            }

            if (state.hasCheckpoint && result.checkpointFloor > 0) {
                Spacer(modifier = Modifier.height(14.dp))
                GlassCard(
                    radius = 16.dp,
                    elevation = 8.dp,
                    fill = SurfaceRaised.copy(alpha = 0.6f),
                    glowColor = ElectricCyan,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_2),
                            contentDescription = stringResource(R.string.cd_anchor),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = stringResource(
                                R.string.anchor_resume,
                                result.checkpointFloor
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(26.dp))
            PrimaryButton(
                label = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                iconPainter = painterResource(R.drawable.ic_refresh),
                iconDescription = null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                label = stringResource(R.string.menu),
                onClick = onMenu,
                iconPainter = painterResource(R.drawable.icon_home),
                iconDescription = stringResource(R.string.cd_home),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StarCondition(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = MutedText,
        textAlign = TextAlign.Center,
        maxLines = 1
    )
}
