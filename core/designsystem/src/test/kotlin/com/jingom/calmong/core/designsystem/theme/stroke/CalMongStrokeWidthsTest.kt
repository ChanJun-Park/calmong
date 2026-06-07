package com.jingom.calmong.core.designsystem.theme.stroke

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class CalMongStrokeWidthsTest {
    @Test
    fun `primitive stroke width matches the selected Figma borderWidth values`() {
        assertEquals(0.dp, CalMongStrokeWidth.none)
        assertEquals(1.dp, CalMongStrokeWidth.hairline)
        assertEquals(2.dp, CalMongStrokeWidth.thin)
        assertEquals(4.dp, CalMongStrokeWidth.thick)
    }

    @Test
    fun `semantic stroke width maps roles to the primitive scale`() {
        assertEquals(0.dp, DefaultCalMongStrokeWidths.none)
        assertEquals(1.dp, DefaultCalMongStrokeWidths.default)
        assertEquals(2.dp, DefaultCalMongStrokeWidths.emphasis)
        assertEquals(4.dp, DefaultCalMongStrokeWidths.focus)
    }
}
