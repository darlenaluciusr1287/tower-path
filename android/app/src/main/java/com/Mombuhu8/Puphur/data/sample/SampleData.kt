package com.Mombuhu8.Puphur.data.sample

import com.Mombuhu8.Puphur.domain.model.DailyEntry
import com.Mombuhu8.Puphur.domain.model.TowerRegion
import com.Mombuhu8.Puphur.domain.model.UnlockItem
import com.Mombuhu8.Puphur.domain.model.TowerRegions
import com.Mombuhu8.Puphur.domain.model.UnlockKind

object SampleData {

    val regions: List<TowerRegion> = listOf(
        TowerRegion(TowerRegions.HARBOUR, 1, 5),
        TowerRegion(TowerRegions.GLASS, 6, 10),
        TowerRegion(TowerRegions.SIGNAL, 11, 15)
    )

    val unlocks: List<UnlockItem> = listOf(
        UnlockItem("cobalt_steel", UnlockKind.MATERIAL, 6),
        UnlockItem("signal_mast", UnlockKind.DECORATION, 12),
        UnlockItem("dawn_harbour", UnlockKind.THEME, 18),
        UnlockItem("cyan_glass", UnlockKind.MATERIAL, 26),
        UnlockItem("crown_beacon", UnlockKind.DECORATION, 34),
        UnlockItem("deep_night", UnlockKind.THEME, 42)
    )

    fun seedDailyHistory(epochDay: Long): List<DailyEntry> = listOf(
        DailyEntry(epochDay - 1L, 1860, 64),
        DailyEntry(epochDay - 2L, 1420, 51),
        DailyEntry(epochDay - 3L, 2180, 73)
    )
}
