package com.Mombuhu8.Puphur.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.R

@Composable
fun StarRow(
    earned: Int,
    total: Int,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp,
    gap: Dp = 6.dp,
    animated: Boolean = false
) {
    val earnedLabel = stringResource(R.string.cd_star_earned)
    val lockedLabel = stringResource(R.string.cd_star_locked)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(gap),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (index in 0 until total) {
            val isEarned = index < earned
            val target = if (!animated || isEarned) 1f else 0.92f
            val scale by animateFloatAsState(
                targetValue = target,
                animationSpec = spring(
                    dampingRatio = 0.5f,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = if (isEarned) earnedLabel else lockedLabel,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(size)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = if (isEarned) 1f else 0.22f
                    }
            )
        }
    }
}
