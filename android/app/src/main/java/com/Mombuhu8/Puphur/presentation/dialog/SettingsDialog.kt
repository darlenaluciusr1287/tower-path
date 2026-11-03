package com.Mombuhu8.Puphur.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.theme.DangerRed
import com.Mombuhu8.Puphur.core.theme.DimText
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.MutedText
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.ui.GlassCard
import com.Mombuhu8.Puphur.core.ui.PrimaryButton
import com.Mombuhu8.Puphur.core.ui.SecondaryButton
import com.Mombuhu8.Puphur.domain.model.AppSettings

@Composable
fun SettingsDialog(
    settings: AppSettings,
    confirmingReset: Boolean,
    onToggleSound: () -> Unit,
    onToggleHaptics: () -> Unit,
    onToggleMotion: () -> Unit,
    onRequestReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        GlassCard(
            radius = 24.dp,
            elevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = LightText
                )
                Spacer(modifier = Modifier.height(16.dp))

                ToggleRow(
                    label = stringResource(R.string.sound),
                    checked = settings.soundEnabled,
                    onToggle = onToggleSound
                )
                ToggleRow(
                    label = stringResource(R.string.haptics),
                    checked = settings.hapticsEnabled,
                    onToggle = onToggleHaptics
                )
                ToggleRow(
                    label = stringResource(R.string.reduced_motion),
                    checked = settings.reducedMotion,
                    onToggle = onToggleMotion
                )

                Spacer(modifier = Modifier.height(14.dp))
                if (confirmingReset) {
                    PrimaryButton(
                        label = stringResource(R.string.reset_confirm),
                        onClick = onConfirmReset,
                        height = 52.dp,
                        gradient = listOf(DangerRed, DangerRed.copy(alpha = 0.72f)),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    SecondaryButton(
                        label = stringResource(R.string.reset_progress),
                        onClick = onRequestReset,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    label = stringResource(R.string.close),
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    val onLabel = stringResource(R.string.on)
    val offLabel = stringResource(R.string.off)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .semantics { stateDescription = if (checked) onLabel else offLabel },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (checked) LightText else MutedText
        )
        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = LightText,
                checkedTrackColor = ElectricCyan,
                uncheckedThumbColor = DimText,
                uncheckedTrackColor = SurfaceRaised
            )
        )
    }
}
