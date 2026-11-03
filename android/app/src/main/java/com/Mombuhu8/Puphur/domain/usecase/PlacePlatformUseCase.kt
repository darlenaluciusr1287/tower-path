package com.Mombuhu8.Puphur.domain.usecase

import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.domain.model.Floor
import com.Mombuhu8.Puphur.domain.model.PlacementResult
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class PlacePlatformUseCase {

    operator fun invoke(
        top: Floor,
        carrierLeft: Float,
        carrierWidth: Float,
        lockedWidth: Float
    ): PlacementResult {
        val overlapLeft = max(top.left, carrierLeft)
        val overlapRight = min(top.left + top.width, carrierLeft + carrierWidth)
        val overlap = overlapRight - overlapLeft
        if (overlap <= 0f) {
            return PlacementResult.Miss
        }

        val offset = abs(
            (carrierLeft + carrierWidth / 2f) - (top.left + top.width / 2f)
        )
        val index = top.index + 1
        val isAnchor = index % GameConfig.ANCHOR_INTERVAL == 0
        val perfect = offset <= GameConfig.PERFECT_TOLERANCE_DP

        val width: Float
        val left: Float
        if (perfect) {
            width = min(top.width + GameConfig.PERFECT_REWARD_DP, lockedWidth)
            left = top.left + (top.width - width) / 2f
        } else {
            width = overlap
            left = overlapLeft
        }

        if (width < GameConfig.MIN_PLAYABLE_WIDTH_DP) {
            return PlacementResult.Miss
        }

        val anchorLocked = isAnchor && offset <= GameConfig.ANCHOR_TOLERANCE_DP
        val sliverWidth = if (perfect) 0f else max(0f, carrierWidth - overlap)
        val sliverLeft = if (carrierLeft < overlapLeft) carrierLeft else overlapRight

        return PlacementResult.Placed(
            floor = Floor(
                index = index,
                left = left,
                width = width,
                perfect = perfect,
                anchor = isAnchor,
                anchorLocked = anchorLocked
            ),
            perfect = perfect,
            anchorLocked = anchorLocked,
            sliverLeft = sliverLeft,
            sliverWidth = sliverWidth
        )
    }
}
