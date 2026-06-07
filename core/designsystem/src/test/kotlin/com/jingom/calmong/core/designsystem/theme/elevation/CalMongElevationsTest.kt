package com.jingom.calmong.core.designsystem.theme.elevation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.color.darkCalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.lightCalMongColorScheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalMongElevationsTest {
    @Test
    fun `primitive shadows match the Figma token values`() {
        assertTrue(CalMongShadows.none.isEmpty())

        assertEquals(2, CalMongShadows.sm.size)
        assertShadow(
            shadow = CalMongShadows.sm[0],
            radius = 3,
            spread = 0,
            offsetY = 1,
            color = Color(0x1A000000),
        )
        assertShadow(
            shadow = CalMongShadows.sm[1],
            radius = 2,
            spread = -1,
            offsetY = 1,
            color = Color(0x1A000000),
        )

        assertEquals(2, CalMongShadows.base.size)
        assertEquals(2, CalMongShadows.lg.size)
        assertEquals(1, CalMongShadows.xxl.size)
        assertEquals(1, CalMongShadows.inner.size)
    }

    @Test
    fun `light elevations use shadows on the default surface`() {
        val colors = lightCalMongColorScheme()
        val elevations = lightCalMongElevations(colors)

        assertEquals(colors.neutral.background.default, elevations.flat.surface)
        assertEquals(colors.neutral.background.default, elevations.raised1.surface)
        assertEquals(colors.neutral.background.default, elevations.raised2.surface)
        assertEquals(colors.neutral.background.default, elevations.overlay.surface)
        assertEquals(colors.neutral.background.default, elevations.modal.surface)

        assertTrue(elevations.flat.shadows.isEmpty())
        assertEquals(CalMongShadows.sm, elevations.raised1.shadows)
        assertEquals(CalMongShadows.base, elevations.raised2.shadows)
        assertEquals(CalMongShadows.lg, elevations.overlay.shadows)
        assertEquals(CalMongShadows.xxl, elevations.modal.shadows)
    }

    @Test
    fun `dark elevations use raised surfaces and limit shadows to overlays`() {
        val colors = darkCalMongColorScheme()
        val elevations = darkCalMongElevations(colors)

        assertEquals(colors.neutral.background.default, elevations.flat.surface)
        assertEquals(colors.neutral.background.raised1, elevations.raised1.surface)
        assertEquals(colors.neutral.background.raised2, elevations.raised2.surface)
        assertEquals(colors.neutral.background.raised2, elevations.overlay.surface)
        assertEquals(colors.neutral.background.raised2, elevations.modal.surface)

        assertTrue(elevations.flat.shadows.isEmpty())
        assertTrue(elevations.raised1.shadows.isEmpty())
        assertTrue(elevations.raised2.shadows.isEmpty())
        assertEquals(CalMongShadows.sm, elevations.overlay.shadows)
        assertEquals(CalMongShadows.xs, elevations.modal.shadows)
    }

    private fun assertShadow(
        shadow: androidx.compose.ui.graphics.shadow.Shadow,
        radius: Int,
        spread: Int,
        offsetY: Int,
        color: Color,
    ) {
        assertEquals(radius.dp, shadow.radius)
        assertEquals(spread.dp, shadow.spread)
        assertEquals(DpOffset(0.dp, offsetY.dp), shadow.offset)
        assertEquals(color, shadow.color)
    }
}
