package com.Mombuhu8.Puphur.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Mombuhu8.Puphur.R
import com.Mombuhu8.Puphur.core.config.GameConfig
import com.Mombuhu8.Puphur.core.theme.DangerRed
import com.Mombuhu8.Puphur.core.theme.ElectricCyan
import com.Mombuhu8.Puphur.core.theme.SurfaceRaised
import com.Mombuhu8.Puphur.core.theme.TowerBlue
import com.Mombuhu8.Puphur.core.theme.TowerBlueDeep
import com.Mombuhu8.Puphur.core.theme.WarmOrange
import com.Mombuhu8.Puphur.domain.model.Floor

@Composable
fun TowerColumn(
    floors: List<Floor>,
    carrierWidth: Float,
    carrierVisible: Boolean,
    hookDim: Boolean,
    failFlash: Boolean,
    carrierX: () -> Float,
    modifier: Modifier = Modifier
) {
    val visible = floors.takeLast(GameConfig.VISIBLE_FLOORS)
    Column(
        modifier = modifier.width(GameConfig.TOWER_VIEWPORT_DP.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(GameConfig.CARRIER_LANE_DP.dp)
        ) {
            if (carrierVisible) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .size(40.dp)
                        .graphicsLayer {
                            translationX = (carrierX() + (carrierWidth - 40f) / 2f).dp.toPx()
                            alpha = if (hookDim) 0.5f else 1f
                        }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .graphicsLayer { translationX = carrierX().dp.toPx() }
                        .width(carrierWidth.dp)
                        .height(GameConfig.FLOOR_HEIGHT_DP.dp)
                        .shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(5.dp),
                            ambientColor = ElectricCyan,
                            spotColor = ElectricCyan
                        )
                        .background(
                            Brush.horizontalGradient(listOf(ElectricCyan, TowerBlue)),
                            RoundedCornerShape(5.dp)
                        )
                        .border(1.dp, Color(0x66FFFFFF), RoundedCornerShape(5.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(GameConfig.CARRIER_GAP_DP.dp))

        visible.reversed().forEachIndexed { position, floor ->
            FloorRow(
                floor = floor,
                depth = position,
                danger = failFlash && position == 0
            )
        }
    }
}

@Composable
private fun FloorRow(floor: Floor, depth: Int, danger: Boolean) {
    val shape = RoundedCornerShape(5.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(GameConfig.FLOOR_HEIGHT_DP.dp)
    ) {
        Spacer(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(1.dp)
                .background(SurfaceRaised.copy(alpha = 0.22f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = floor.left.dp)
                .width(floor.width.dp)
                .height(GameConfig.FLOOR_HEIGHT_DP.dp)
                .shadow(
                    elevation = (2 + depth % 4).dp,
                    shape = shape,
                    ambientColor = TowerBlue,
                    spotColor = TowerBlue
                )
                .background(
                    brush = when {
                        danger -> Brush.horizontalGradient(
                            listOf(DangerRed, DangerRed.copy(alpha = 0.8f))
                        )

                        floor.anchor -> Brush.horizontalGradient(
                            listOf(WarmOrange, TowerBlue)
                        )

                        else -> Brush.horizontalGradient(
                            listOf(TowerBlue, TowerBlueDeep)
                        )
                    },
                    shape = shape
                )
                .border(
                    width = if (floor.perfect) 1.5.dp else 1.dp,
                    color = if (floor.perfect) ElectricCyan else Color(0x3354D6C8),
                    shape = shape
                )
        ) {
            if (floor.anchor) {
                Image(
                    painter = painterResource(R.drawable.sprite_2),
                    contentDescription = stringResource(R.string.cd_anchor),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(22.dp)
                )
            }
        }
    }
}
