package com.Mombuhu8.Puphur.presentation.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.di.ServiceLocator
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.MutedText
import com.Mombuhu8.Puphur.core.theme.StrokeGlass
import com.Mombuhu8.Puphur.core.theme.SurfaceDeepBlue
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.TowerBlue
import com.Mombuhu8.Puphur.core.theme.WarmOrange
import com.Mombuhu8.Puphur.core.theme.MenuOverlay
import com.Mombuhu8.Puphur.core.ui.GlassCard
import com.Mombuhu8.Puphur.core.ui.PrimaryButton
import com.Mombuhu8.Puphur.core.ui.ScreenBackground
import com.Mombuhu8.Puphur.core.ui.SecondaryButton
import com.Mombuhu8.Puphur.core.ui.StarRow
import com.Mombuhu8.Puphur.core.ui.modeLabel
import com.Mombuhu8.Puphur.core.ui.regionLabel
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.presentation.dialog.SettingsDialog

@Composable
fun MenuRoute(onPlay: (Int, GameMode) -> Unit) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.factory())
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.refresh()
    }

    MenuScreen(
        state = state,
        onPlay = { onPlay(state.nextStageId, state.selectedMode) },
        onSelectMode = menuViewModel::selectMode,
        onOpenPanel = menuViewModel::openPanel,
        onClosePanel = menuViewModel::closePanel,
        onSelectStage = menuViewModel::selectStage,
        onToggleSound = menuViewModel::toggleSound,
        onToggleHaptics = menuViewModel::toggleHaptics,
        onToggleMotion = menuViewModel::toggleReducedMotion,
        onRequestReset = menuViewModel::requestReset,
        onConfirmReset = menuViewModel::confirmReset
    )
}

@Composable
private fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onSelectMode: (GameMode) -> Unit,
    onOpenPanel: (MenuPanel) -> Unit,
    onClosePanel: () -> Unit,
    onSelectStage: (Int) -> Unit,
    onToggleSound: () -> Unit,
    onToggleHaptics: () -> Unit,
    onToggleMotion: () -> Unit,
    onRequestReset: () -> Unit,
    onConfirmReset: () -> Unit
) {
    ScreenBackground(overlay = MenuOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                GlassCard(
                    radius = 18.dp,
                    elevation = 10.dp,
                    glowColor = WarmOrange,
                    borderColor = WarmOrange.copy(alpha = 0.45f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 18.dp, top = 12.dp)
                        .height(36.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_4),
                            contentDescription = stringResource(R.string.cd_total_stars),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = state.totalStars.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = FontFamily.Monospace,
                            color = WarmOrange
                        )
                    }
                }

                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = stringResource(R.string.cd_destination),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 28.dp, bottom = 18.dp)
                        .size(72.dp)
                )

                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 64.dp)
                        .size(56.dp)
                )
            }

            AnimatedVisibility(
                visible = !state.loading,
                enter = slideInVertically(
                    animationSpec = tween(360),
                    initialOffsetY = { it / 3 }
                ) + fadeIn(tween(360))
            ) {
                MenuSheet(
                    state = state,
                    onPlay = onPlay,
                    onSelectMode = onSelectMode,
                    onOpenPanel = onOpenPanel
                )
            }
        }

        when (state.panel) {
            MenuPanel.MAP -> TowerMapPanel(
                stages = state.stages,
                onSelectStage = onSelectStage,
                onDismiss = onClosePanel
            )

            MenuPanel.RECORDS -> RecordsPanel(
                summary = state.records,
                totalStars = state.totalStars,
                onDismiss = onClosePanel
            )

            MenuPanel.COLLECTION -> CollectionPanel(
                unlocks = state.unlocks,
                totalStars = state.totalStars,
                onDismiss = onClosePanel
            )

            MenuPanel.SETTINGS -> SettingsDialog(
                settings = state.settings,
                confirmingReset = state.confirmingReset,
                onToggleSound = onToggleSound,
                onToggleHaptics = onToggleHaptics,
                onToggleMotion = onToggleMotion,
                onRequestReset = onRequestReset,
                onConfirmReset = onConfirmReset,
                onDismiss = onClosePanel
            )

            MenuPanel.NONE -> Unit
        }
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    onPlay: () -> Unit,
    onSelectMode: (GameMode) -> Unit,
    onOpenPanel: (MenuPanel) -> Unit
) {
    val shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(28.dp, shape, ambientColor = TowerBlue, spotColor = TowerBlue)
            .background(SurfaceDeepBlue.copy(alpha = 0.94f), shape)
            .border(1.dp, StrokeGlass, shape)
            .padding(start = 22.dp, end = 22.dp, top = 18.dp, bottom = 20.dp)
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(44.dp)
                .height(4.dp)
                .background(StrokeGlass, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.height(14.dp))

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(132.dp)
                .height(52.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.wordmark),
            style = MaterialTheme.typography.headlineMedium,
            color = LightText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.menu_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText
        )

        Spacer(modifier = Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ModeChip(
                mode = GameMode.CAMPAIGN,
                selected = state.selectedMode == GameMode.CAMPAIGN,
                onSelectMode = onSelectMode,
                modifier = Modifier.weight(1f)
            )
            ModeChip(
                mode = GameMode.DAILY,
                selected = state.selectedMode == GameMode.DAILY,
                onSelectMode = onSelectMode,
                modifier = Modifier.weight(1f)
            )
            ModeChip(
                mode = GameMode.PRACTICE,
                selected = state.selectedMode == GameMode.PRACTICE,
                onSelectMode = onSelectMode,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        GlassCard(
            radius = 18.dp,
            elevation = 8.dp,
            fill = SurfaceRaised.copy(alpha = 0.72f),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .clickable { onOpenPanel(MenuPanel.MAP) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(
                            R.string.stage_line,
                            state.nextStageId,
                            regionLabel(state.nextStageRegionKey)
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = LightText
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.goal_floors, state.nextStageGoal),
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricCyan
                    )
                }
                StarRow(earned = state.nextStageStars, total = 3, size = 18.dp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            label = stringResource(R.string.play),
            onClick = onPlay,
            iconPainter = painterResource(R.drawable.ic_play),
            iconDescription = null,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SecondaryButton(
                label = stringResource(R.string.tower_map),
                onClick = { onOpenPanel(MenuPanel.MAP) },
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                label = stringResource(R.string.records),
                onClick = { onOpenPanel(MenuPanel.RECORDS) },
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                label = stringResource(R.string.collection),
                onClick = { onOpenPanel(MenuPanel.COLLECTION) },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onOpenPanel(MenuPanel.SETTINGS) },
                modifier = Modifier
                    .size(48.dp)
                    .background(SurfaceRaised.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
                    .border(1.dp, StrokeGlass, RoundedCornerShape(16.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings),
                    tint = MutedText,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun ModeChip(
    mode: GameMode,
    selected: Boolean,
    onSelectMode: (GameMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(21.dp)
    val chipLabel = modeLabel(mode)
    val selectedState = stringResource(R.string.sd_selected)
    val idleState = stringResource(R.string.sd_not_selected)
    Box(
        modifier = modifier
            .height(42.dp)
            .background(
                color = if (selected) {
                    TowerBlue.copy(alpha = 0.18f)
                } else {
                    SurfaceRaised.copy(alpha = 0.45f)
                },
                shape = shape
            )
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) TowerBlue else StrokeGlass,
                shape = shape
            )
            .clickable { onSelectMode(mode) }
            .semantics {
                contentDescription = chipLabel
                stateDescription = if (selected) selectedState else idleState
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = chipLabel,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            color = if (selected) LightText else MutedText
        )
    }
}
