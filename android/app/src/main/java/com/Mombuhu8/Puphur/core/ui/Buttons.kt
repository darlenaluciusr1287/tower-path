package com.Mombuhu8.Puphur.core.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.core.theme.DimText
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.MutedText
import com.Mombuhu8.Puphur.core.theme.StrokeGlass
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.TowerBlue

@Composable
fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 60.dp,
    iconPainter: Painter? = null,
    iconDescription: String? = null,
    gradient: List<Color> = listOf(TowerBlue, ElectricCyan)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = Spring.StiffnessMedium)
    )
    val shape = RoundedCornerShape(18.dp)
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        elevation = null,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = LightText,
            disabledContentColor = DimText
        ),
        modifier = modifier
            .heightIn(min = 48.dp)
            .height(height)
            .semantics { contentDescription = label }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 16.dp else 0.dp,
                shape = shape,
                ambientColor = TowerBlue,
                spotColor = TowerBlue
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (enabled) {
                        Brush.horizontalGradient(gradient)
                    } else {
                        Brush.horizontalGradient(listOf(SurfaceRaised, SurfaceRaised))
                    },
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (iconPainter != null) {
                    Image(
                        painter = iconPainter,
                        contentDescription = iconDescription,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                    color = if (enabled) LightText else DimText
                )
            }
        }
    }
}

@Composable
fun SecondaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp = 48.dp,
    iconPainter: Painter? = null,
    iconDescription: String? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium)
    )
    val shape = RoundedCornerShape(16.dp)
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 10.dp),
        border = null,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = LightText,
            disabledContentColor = DimText
        ),
        modifier = modifier
            .heightIn(min = 48.dp)
            .height(height)
            .semantics { contentDescription = label }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .background(
                color = if (enabled) {
                    SurfaceRaised.copy(alpha = 0.55f)
                } else {
                    SurfaceRaised.copy(alpha = 0.3f)
                },
                shape = shape
            )
            .border(1.dp, if (enabled) StrokeGlass else DimText.copy(alpha = 0.4f), shape)
    ) {
        if (iconPainter != null) {
            Image(
                painter = iconPainter,
                contentDescription = iconDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = if (enabled) MutedText else DimText
        )
    }
}
