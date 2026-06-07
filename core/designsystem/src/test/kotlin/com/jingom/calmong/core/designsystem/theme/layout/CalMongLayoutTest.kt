package com.jingom.calmong.core.designsystem.theme.layout

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class CalMongLayoutTest {
    @Test
    fun `breakpoint primitives use Android dp thresholds`() {
        assertEquals(600.dp, CalMongBreakpoints.medium)
        assertEquals(840.dp, CalMongBreakpoints.expanded)
        assertEquals(1200.dp, CalMongBreakpoints.large)
    }

    @Test
    fun `fromWidth classifies window width at boundaries`() {
        assertEquals(WindowWidthClass.Compact, WindowWidthClass.fromWidth(599.dp))
        assertEquals(WindowWidthClass.Medium, WindowWidthClass.fromWidth(600.dp))
        assertEquals(WindowWidthClass.Medium, WindowWidthClass.fromWidth(839.dp))
        assertEquals(WindowWidthClass.Expanded, WindowWidthClass.fromWidth(840.dp))
        assertEquals(WindowWidthClass.Expanded, WindowWidthClass.fromWidth(1199.dp))
        assertEquals(WindowWidthClass.Large, WindowWidthClass.fromWidth(1200.dp))
    }

    @Test
    fun `contentMaxWidth maps roles to the container scale`() {
        assertEquals(448.dp, DefaultCalMongLayout.contentMaxWidth.form)
        assertEquals(672.dp, DefaultCalMongLayout.contentMaxWidth.prose)
        assertEquals(768.dp, DefaultCalMongLayout.contentMaxWidth.wide)
    }
}
