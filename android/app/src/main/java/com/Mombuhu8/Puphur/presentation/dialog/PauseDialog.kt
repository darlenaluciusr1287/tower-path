package com.Mombuhu8.Puphur.presentation.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.theme.LightText
import com.Mombuhu8.Puphur.core.theme.MutedText
import com.Mombuhu8.Puphur.core.ui.GlassCard
import com.Mombuhu8.Puphur.core.ui.PrimaryButton
import com.Mombuhu8.Puphur.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        GlassCard(
            radius = 24.dp,
            elevation = 24.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.pause_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = LightText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.pause_hint),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText
                )
                Spacer(modifier = Modifier.height(18.dp))
                PrimaryButton(
                    label = stringResource(R.string.resume),
                    onClick = onResume,
                    height = 56.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    label = stringResource(R.string.restart_stage),
                    onClick = onRestart,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    label = stringResource(R.string.menu),
                    onClick = onMenu,
                    iconPainter = painterResource(R.drawable.icon_home),
                    iconDescription = stringResource(R.string.cd_home),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
