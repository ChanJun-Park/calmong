package com.jingom.calmong.core.designsystem.theme.elevation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Figma `shadow.json`을 Compose [Shadow] 목록으로 옮긴 primitive shadow scale.
 *
 * 제품 코드에서는 직접 사용하지 않고 [CalMongElevations]의 semantic 역할을 사용한다.
 */
internal object CalMongShadows {
    val none = emptyList<Shadow>()

    val xxs =
        listOf(
            shadow(
                color = Color(0x0D000000),
                offsetY = 1,
                radius = 0,
            ),
        )

    val xs =
        listOf(
            shadow(
                color = Color(0x0D000000),
                offsetY = 1,
                radius = 2,
            ),
        )

    val sm =
        listOf(
            shadow(
                color = Color(0x1A000000),
                offsetY = 1,
                radius = 3,
            ),
            shadow(
                color = Color(0x1A000000),
                offsetY = 1,
                radius = 2,
                spread = -1,
            ),
        )

    val base =
        listOf(
            shadow(
                color = Color(0x1A000000),
                offsetY = 4,
                radius = 6,
                spread = -1,
            ),
            shadow(
                color = Color(0x1A000000),
                offsetY = 2,
                radius = 4,
                spread = -2,
            ),
        )

    val md =
        listOf(
            shadow(
                color = Color(0x1A000000),
                offsetY = 10,
                radius = 15,
                spread = -3,
            ),
            shadow(
                color = Color(0x1A000000),
                offsetY = 4,
                radius = 6,
                spread = -4,
            ),
        )

    val lg =
        listOf(
            shadow(
                color = Color(0x1A000000),
                offsetY = 20,
                radius = 25,
                spread = -5,
            ),
            shadow(
                color = Color(0x1A000000),
                offsetY = 8,
                radius = 10,
                spread = -6,
            ),
        )

    /** Figma `xl`은 `lg`와 값이 같으므로 별도 semantic 역할로 노출하지 않는다. */
    val xl = lg

    val xxl =
        listOf(
            shadow(
                color = Color(0x40000000),
                offsetY = 25,
                radius = 50,
                spread = -12,
            ),
        )

    val inner =
        listOf(
            shadow(
                color = Color(0x0D000000),
                offsetY = 2,
                radius = 4,
            ),
        )

    private fun shadow(
        color: Color,
        offsetY: Int,
        radius: Int,
        spread: Int = 0,
    ) = Shadow(
        radius = radius.dp,
        color = color,
        spread = spread.dp,
        offset = DpOffset(0.dp, offsetY.dp),
    )
}
