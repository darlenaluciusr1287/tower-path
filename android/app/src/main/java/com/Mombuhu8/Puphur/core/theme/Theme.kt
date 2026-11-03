package com.Mombuhu8.Puphur.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val TowerColorScheme = darkColorScheme(
    primary = TowerBlue,
    onPrimary = BgVoid,
    primaryContainer = TowerBlueDeep,
    onPrimaryContainer = LightText,
    secondary = ElectricCyan,
    onSecondary = BgVoid,
    secondaryContainer = SurfaceRaised,
    onSecondaryContainer = LightText,
    tertiary = WarmOrange,
    onTertiary = BgVoid,
    background = BgNight,
    onBackground = LightText,
    surface = SurfaceDeepBlue,
    onSurface = LightText,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = MutedText,
    outline = StrokeGlass,
    outlineVariant = StrokeGlass,
    error = DangerRed,
    onError = BgVoid
)

private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TowerColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
