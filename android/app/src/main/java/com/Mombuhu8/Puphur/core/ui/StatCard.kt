package com.Mombuhu8.Puphur.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.core.theme.DimText

@Composable
fun StatCard(
    value: String,
    label: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        radius = 16.dp,
        elevation = 8.dp,
        borderColor = accent.copy(alpha = 0.34f),
        glowColor = accent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(
                modifier = Modifier
                    .size(8.dp)
                    .background(accent, CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace,
                color = accent,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = DimText,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
