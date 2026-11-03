package com.Mombuhu8.Puphur.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.di.ServiceLocator
import com.Mombuhu8.Puphur.core.theme.DimText
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.MutedText
import com.Mombuhu8.Puphur.core.theme.SplashOverlay
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.TowerBlue
import com.Mombuhu8.Puphur.core.ui.ScreenBackground

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.factory())
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen() {
    val transition = rememberInfiniteTransition()
    val glow by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val bob by transition.animateFloat(
        initialValue = 0f,
        targetValue = -7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    val twinkle by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    ScreenBackground(overlay = SplashOverlay) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(ElectricCyan.copy(alpha = 0.20f), Color.Transparent),
                        radius = 480f
                    )
                )
        )

        StarField(twinkle = twinkle)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .height(258.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(258.dp)
                        .clip(RoundedCornerShape(24.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .graphicsLayer {
                                translationY = sweep * 258.dp.toPx()
                                alpha = 0.45f
                            }
                            .background(ElectricCyan)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(176.dp)
                            .height(112.dp)
                            .graphicsLayer { alpha = glow }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = painterResource(R.drawable.sprite_5),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(44.dp)
                            .graphicsLayer { translationY = bob.dp.toPx() }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    TowerSlab(widthDp = 82)
                    TowerSlab(widthDp = 100)
                    TowerSlab(widthDp = 118)
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.wordmark),
                style = MaterialTheme.typography.displayLarge,
                color = LightText,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.tagline),
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            LinearProgressIndicator(
                color = ElectricCyan,
                trackColor = SurfaceRaised,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .width(168.dp)
                    .height(4.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.loader_caption),
                style = MaterialTheme.typography.labelSmall,
                color = DimText,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun TowerSlab(widthDp: Int) {
    Image(
        painter = painterResource(R.drawable.sprite_1),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .width(widthDp.dp)
            .height(26.dp)
            .shadow(14.dp, RoundedCornerShape(6.dp), ambientColor = TowerBlue, spotColor = TowerBlue)
    )
}

@Composable
private fun StarField(twinkle: Float) {
    val dots = listOf(
        Triple(38, 120, 0.9f),
        Triple(300, 180, 0.6f),
        Triple(96, 640, 0.75f),
        Triple(268, 700, 0.5f),
        Triple(172, 90, 0.45f),
        Triple(324, 520, 0.8f)
    )
    Box(modifier = Modifier.fillMaxSize()) {
        dots.forEach { dot ->
            Box(
                modifier = Modifier
                    .offset(x = dot.first.dp, y = dot.second.dp)
                    .size(3.dp)
                    .graphicsLayer { alpha = twinkle * dot.third }
                    .background(ElectricCyan, CircleShape)
            )
        }
    }
}
