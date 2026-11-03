package com.Mombuhu8.Puphur.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.theme.DimText
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.WarmOrange
import com.Mombuhu8.Puphur.core.ui.GlassCard
import com.Mombuhu8.Puphur.core.ui.unlockKindLabel
import com.Mombuhu8.Puphur.core.ui.unlockLabel
import com.Mombuhu8.Puphur.domain.model.UnlockKind

@Composable
fun CollectionPanel(
    unlocks: List<UnlockEntry>,
    totalStars: Int,
    onDismiss: () -> Unit
) {
    PanelDialog(title = stringResource(R.string.collection_title), onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = stringResource(R.string.cd_total_stars),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = stringResource(R.string.unlock_cost, totalStars),
                    style = MaterialTheme.typography.labelSmall,
                    color = WarmOrange
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            unlocks.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { entry ->
                        UnlockTile(entry = entry, modifier = Modifier.weight(1f))
                    }
                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun UnlockTile(entry: UnlockEntry, modifier: Modifier = Modifier) {
    val lockedState = stringResource(R.string.sd_locked)
    val openState = stringResource(R.string.sd_unlocked)
    val sprite = when (entry.kind) {
        UnlockKind.MATERIAL -> R.drawable.sprite_1
        UnlockKind.DECORATION -> R.drawable.sprite_2
        UnlockKind.THEME -> R.drawable.sprite_5
    }
    GlassCard(
        modifier = modifier
            .height(132.dp)
            .semantics { stateDescription = if (entry.unlocked) openState else lockedState },
        radius = 16.dp,
        elevation = 8.dp,
        fill = SurfaceRaised.copy(alpha = 0.55f),
        glowColor = if (entry.unlocked) ElectricCyan else DimText
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(sprite),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(56.dp)
                        .graphicsLayer { alpha = if (entry.unlocked) 1f else 0.3f }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = unlockLabel(entry.key),
                style = MaterialTheme.typography.labelSmall,
                color = if (entry.unlocked) LightText else DimText,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (entry.unlocked) {
                    unlockKindLabel(entry.kind)
                } else {
                    stringResource(R.string.unlock_cost, entry.starCost)
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (entry.unlocked) ElectricCyan else WarmOrange,
                maxLines = 1
            )
        }
    }
}
