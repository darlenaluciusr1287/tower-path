package com.Mombuhu8.Puphur.presentation.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.SurfaceDeepBlue
import com.Mombuhu8.Puphur.core.ui.GlassCard
import com.Mombuhu8.Puphur.core.ui.briefLabel
import com.Mombuhu8.Puphur.domain.model.StageBrief

@Composable
fun StageBriefPanel(
    brief: StageBrief,
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible && brief != StageBrief.NONE,
        enter = fadeIn(tween(280)),
        exit = fadeOut(tween(280)),
        modifier = modifier
    ) {
        GlassCard(
            radius = 16.dp,
            elevation = 12.dp,
            fill = SurfaceDeepBlue.copy(alpha = 0.90f),
            glowColor = ElectricCyan,
            borderColor = ElectricCyan.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = briefLabel(brief),
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricCyan,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
