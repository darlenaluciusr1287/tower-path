package com.Mombuhu8.Puphur.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.theme.DimText
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.StrokeGlass
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.TowerBlue
import com.Mombuhu8.Puphur.core.ui.GlassCard
import com.Mombuhu8.Puphur.core.ui.SecondaryButton
import com.Mombuhu8.Puphur.core.ui.StarRow
import com.Mombuhu8.Puphur.core.ui.regionLabel

@Composable
fun TowerMapPanel(
    stages: List<StageEntry>,
    onSelectStage: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    PanelDialog(title = stringResource(R.string.tower_map_title), onDismiss = onDismiss) {
        stages.groupBy { it.regionKey }.forEach { (regionKey, group) ->
            Text(
                text = regionLabel(regionKey),
                style = MaterialTheme.typography.labelSmall,
                color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                group.forEach { entry ->
                    StageTile(
                        entry = entry,
                        onSelectStage = onSelectStage,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StageTile(
    entry: StageEntry,
    onSelectStage: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(14.dp)
    val lockedState = stringResource(R.string.sd_locked)
    val openState = stringResource(R.string.sd_unlocked)
    Box(
        modifier = modifier
            .height(78.dp)
            .background(
                color = if (entry.unlocked) {
                    TowerBlue.copy(alpha = 0.16f)
                } else {
                    SurfaceRaised.copy(alpha = 0.40f)
                },
                shape = shape
            )
            .border(
                width = 1.dp,
                color = if (entry.unlocked) TowerBlue else StrokeGlass,
                shape = shape
            )
            .clickable(enabled = entry.unlocked) { onSelectStage(entry.stageId) }
            .semantics { stateDescription = if (entry.unlocked) openState else lockedState },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = entry.stageId.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace,
                color = if (entry.unlocked) LightText else DimText
            )
            Text(
                text = stringResource(R.string.goal_floors, entry.goalFloors),
                style = MaterialTheme.typography.labelSmall,
                color = DimText
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (entry.unlocked) {
                StarRow(earned = entry.stars, total = 3, size = 12.dp, gap = 3.dp)
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = lockedState,
                    tint = DimText,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun PanelDialog(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            radius = 24.dp,
            elevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = LightText
                )
                Spacer(modifier = Modifier.height(16.dp))
                content()
                Spacer(modifier = Modifier.height(4.dp))
                SecondaryButton(
                    label = stringResource(R.string.close),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
