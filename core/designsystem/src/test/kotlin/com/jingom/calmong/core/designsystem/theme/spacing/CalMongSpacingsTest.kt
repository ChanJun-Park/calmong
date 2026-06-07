package com.jingom.calmong.core.designsystem.theme.spacing

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class CalMongSpacingsTest {
    @Test
    fun `primitive spacing matches the selected Figma token values`() {
        assertEquals(0.dp, CalMongSpacing.none)
        assertEquals(2.dp, CalMongSpacing.xxs)
        assertEquals(4.dp, CalMongSpacing.xs)
        assertEquals(8.dp, CalMongSpacing.sm)
        assertEquals(12.dp, CalMongSpacing.md)
        assertEquals(16.dp, CalMongSpacing.lg)
        assertEquals(20.dp, CalMongSpacing.xl)
        assertEquals(24.dp, CalMongSpacing.xxl)
        assertEquals(32.dp, CalMongSpacing.xxxl)
        assertEquals(40.dp, CalMongSpacing.huge)
        assertEquals(48.dp, CalMongSpacing.massive)
        assertEquals(64.dp, CalMongSpacing.section)
    }

    @Test
    fun `semantic spacing maps roles to the primitive scale`() {
        assertEquals(4.dp, DefaultCalMongSpacings.gap.tight)
        assertEquals(8.dp, DefaultCalMongSpacings.gap.compact)
        assertEquals(12.dp, DefaultCalMongSpacings.gap.default)
        assertEquals(16.dp, DefaultCalMongSpacings.gap.relaxed)
        assertEquals(24.dp, DefaultCalMongSpacings.gap.loose)

        assertEquals(8.dp, DefaultCalMongSpacings.inset.compact)
        assertEquals(16.dp, DefaultCalMongSpacings.inset.default)
        assertEquals(20.dp, DefaultCalMongSpacings.inset.comfortable)
        assertEquals(24.dp, DefaultCalMongSpacings.inset.spacious)

        assertEquals(24.dp, DefaultCalMongSpacings.section.related)
        assertEquals(32.dp, DefaultCalMongSpacings.section.default)
        assertEquals(48.dp, DefaultCalMongSpacings.section.distinct)
        assertEquals(64.dp, DefaultCalMongSpacings.section.page)
    }
}
