package com.jingom.calmong.core.designsystem.theme.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class CalMongShapesTest {
    @Test
    fun `primitive radius matches the Figma token scale`() {
        assertEquals(0.dp, CalMongRadius.none)
        assertEquals(2.dp, CalMongRadius.sm)
        assertEquals(4.dp, CalMongRadius.base)
        assertEquals(6.dp, CalMongRadius.md)
        assertEquals(8.dp, CalMongRadius.lg)
        assertEquals(12.dp, CalMongRadius.xl)
        assertEquals(16.dp, CalMongRadius.xxl)
        assertEquals(24.dp, CalMongRadius.xxxl)
        assertEquals(999.dp, CalMongRadius.full)
    }

    @Test
    fun `semantic shapes map roles to the radius scale`() {
        assertEquals(RoundedCornerShape(6.dp), DefaultCalMongShapes.control.compact)
        assertEquals(RoundedCornerShape(8.dp), DefaultCalMongShapes.control.default)
        assertEquals(RoundedCornerShape(12.dp), DefaultCalMongShapes.control.large)
        assertEquals(RoundedCornerShape(999.dp), DefaultCalMongShapes.control.pill)

        assertEquals(RoundedCornerShape(8.dp), DefaultCalMongShapes.surface.small)
        assertEquals(RoundedCornerShape(12.dp), DefaultCalMongShapes.surface.card)
        assertEquals(RoundedCornerShape(16.dp), DefaultCalMongShapes.surface.elevated)
        assertEquals(
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
            ),
            DefaultCalMongShapes.surface.sheet,
        )
        assertEquals(RoundedCornerShape(24.dp), DefaultCalMongShapes.surface.dialog)

        assertEquals(RoundedCornerShape(8.dp), DefaultCalMongShapes.content.thumbnail)
        assertEquals(RoundedCornerShape(12.dp), DefaultCalMongShapes.content.image)
        assertEquals(RoundedCornerShape(999.dp), DefaultCalMongShapes.content.avatar)
        assertEquals(RoundedCornerShape(999.dp), DefaultCalMongShapes.content.badge)
    }

    @Test
    fun `Material shapes map to the shared radius scale`() {
        val shapes = calMongMaterialShapes()

        assertEquals(RoundedCornerShape(4.dp), shapes.extraSmall)
        assertEquals(RoundedCornerShape(8.dp), shapes.small)
        assertEquals(RoundedCornerShape(12.dp), shapes.medium)
        assertEquals(RoundedCornerShape(16.dp), shapes.large)
        assertEquals(RoundedCornerShape(24.dp), shapes.extraLarge)
    }
}
