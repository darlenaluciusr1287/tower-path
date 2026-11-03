package com.Mombuhu8.Puphur.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.theme.BgNight

@Composable
fun ScreenBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    accent: Brush? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgNight)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
        )
        if (accent != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(accent)
            )
        }
        content()
    }
}
