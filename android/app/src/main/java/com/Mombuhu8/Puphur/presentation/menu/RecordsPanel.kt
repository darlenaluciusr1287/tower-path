package com.Mombuhu8.Puphur.presentation.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.theme.DimText
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.MutedText
import com.Mombuhu8.Puphur.core.theme.TowerBlue
import com.Mombuhu8.Puphur.core.theme.WarmOrange
import com.Mombuhu8.Puphur.core.ui.StatCard
import com.Mombuhu8.Puphur.domain.model.ScoreFormat

@Composable
fun RecordsPanel(
    summary: RecordsSummary,
    totalStars: Int,
    onDismiss: () -> Unit
) {
    PanelDialog(title = stringResource(R.string.records_title), onDismiss = onDismiss) {
        Column(modifier = Modifier.fillMaxWidth()) {
            val topCards = buildList {
                if (totalStars > 0) {
                    add(
                        CardData(
                            totalStars.toString(),
                            stringResource(R.string.stars_label),
                            WarmOrange
                        )
                    )
                }
                if (summary.bestScore > 0) {
                    add(
                        CardData(
                            ScoreFormat.format(summary.bestScore),
                            stringResource(R.string.best_score),
                            ElectricCyan
                        )
                    )
                }
                if (summary.regionsCleared > 0) {
                    add(
                        CardData(
                            summary.regionsCleared.toString(),
                            stringResource(R.string.regions_cleared),
                            TowerBlue
                        )
                    )
                }
            }

            val bottomCards = buildList {
                if (summary.bestAccuracy > 0) {
                    add(
                        CardData(
                            stringResource(R.string.percent_value, summary.bestAccuracy),
                            stringResource(R.string.best_accuracy),
                            ElectricCyan
                        )
                    )
                }
                if (summary.longestStreak > 0) {
                    add(
                        CardData(
                            summary.longestStreak.toString(),
                            stringResource(R.string.longest_streak),
                            WarmOrange
                        )
                    )
                }
            }

            if (topCards.size >= 2) {
                CardRow(topCards)
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (bottomCards.size >= 2) {
                CardRow(bottomCards)
                Spacer(modifier = Modifier.height(10.dp))
            }
            if (topCards.size < 2 && bottomCards.size < 2) {
                Text(
                    text = stringResource(R.string.no_records),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Text(
                text = stringResource(R.string.daily_history),
                style = MaterialTheme.typography.labelSmall,
                color = ElectricCyan
            )
            Spacer(modifier = Modifier.height(6.dp))
            if (summary.daily.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_records),
                    style = MaterialTheme.typography.bodyMedium,
                    color = DimText
                )
            } else {
                summary.daily.forEach { entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(
                                R.string.daily_entry,
                                entry.epochDay % 1000L,
                                ScoreFormat.format(entry.score)
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedText
                        )
                        Text(
                            text = stringResource(R.string.percent_value, entry.accuracy),
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElectricCyan
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

private data class CardData(
    val value: String,
    val label: String,
    val accent: androidx.compose.ui.graphics.Color
)

@Composable
private fun CardRow(cards: List<CardData>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        cards.forEach { card ->
            StatCard(
                value = card.value,
                label = card.label,
                accent = card.accent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
