package com.Mombuhu8.Puphur.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.core.theme.StrokeGlass
import com.Mombuhu8.Puphur.core.theme.SurfaceDeepBlue
import com.Mombuhu8.Puphur.core.theme.TowerBlue

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    radius: Dp = 20.dp,
    elevation: Dp = 8.dp,
    fill: Color = SurfaceDeepBlue.copy(alpha = 0.82f),
    borderColor: Color = StrokeGlass,
    glowColor: Color = TowerBlue,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Box(
        modifier = modifier
            .shadow(elevation, shape, ambientColor = glowColor, spotColor = glowColor)
            .background(fill, shape)
            .border(1.dp, borderColor, shape),
        content = content
    )
}
