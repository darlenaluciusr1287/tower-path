package com.Mombuhu8.Puphur.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.domain.model.GameMode
import com.Mombuhu8.Puphur.domain.model.StageBrief
import com.Mombuhu8.Puphur.domain.model.TowerRegions
import com.Mombuhu8.Puphur.domain.model.UnlockKind

@Composable
fun regionLabel(key: String): String = stringResource(
    when (key) {
        TowerRegions.HARBOUR -> R.string.region_harbour_spires
        TowerRegions.GLASS -> R.string.region_glass_quarter
        else -> R.string.region_signal_crown
    }
)

@Composable
fun modeLabel(mode: GameMode): String = stringResource(
    when (mode) {
        GameMode.CAMPAIGN -> R.string.mode_campaign
        GameMode.DAILY -> R.string.mode_daily
        GameMode.PRACTICE -> R.string.mode_practice
    }
)

@Composable
fun unlockLabel(key: String): String = stringResource(
    when (key) {
        "cobalt_steel" -> R.string.unlock_cobalt_steel
        "signal_mast" -> R.string.unlock_signal_mast
        "dawn_harbour" -> R.string.unlock_dawn_harbour
        "cyan_glass" -> R.string.unlock_cyan_glass
        "crown_beacon" -> R.string.unlock_crown_beacon
        else -> R.string.unlock_deep_night
    }
)

@Composable
fun unlockKindLabel(kind: UnlockKind): String = stringResource(
    when (kind) {
        UnlockKind.MATERIAL -> R.string.kind_material
        UnlockKind.DECORATION -> R.string.kind_decoration
        UnlockKind.THEME -> R.string.kind_theme
    }
)

@Composable
fun briefLabel(brief: StageBrief): String = stringResource(
    when (brief) {
        StageBrief.ANCHOR -> R.string.brief_anchor
        StageBrief.WIND -> R.string.brief_wind
        StageBrief.HITCH -> R.string.brief_hitch
        else -> R.string.brief_overlap
    }
)
