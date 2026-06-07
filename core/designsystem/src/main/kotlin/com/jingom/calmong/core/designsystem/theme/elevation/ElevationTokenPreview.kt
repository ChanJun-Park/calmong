@file:Suppress("UnusedPrivateMember")

package com.jingom.calmong.core.designsystem.theme.elevation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

@Preview(name = "Elevation tokens · Light", showBackground = true)
@Composable
private fun ElevationTokensLightPreview() {
    CalMongTheme(darkTheme = false) { ElevationTokenBoard() }
}

@Preview(name = "Elevation tokens · Dark", showBackground = true)
@Composable
private fun ElevationTokensDarkPreview() {
    CalMongTheme(darkTheme = true) { ElevationTokenBoard() }
}

@Composable
private fun ElevationTokenBoard() {
    val elevations = CalMongTheme.elevations
    val shape = CalMongTheme.shapes.surface.card

    Surface(color = CalMongTheme.colors.neutral.background.base) {
        Column(
            modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            ElevationSample("flat", elevations.flat, shape)
            ElevationSample("raised1", elevations.raised1, shape)
            ElevationSample("raised2", elevations.raised2, shape)
            ElevationSample("overlay", elevations.overlay, shape)
            ElevationSample("modal", elevations.modal, shape)
        }
    }
}

@Composable
private fun ElevationSample(
    label: String,
    elevation: ElevationStyle,
    shape: Shape,
) {
    Surface(
        modifier =
            Modifier
                .size(width = 240.dp, height = 72.dp)
                .elevationShadow(elevation, shape),
        shape = shape,
        color = elevation.surface,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(20.dp),
            color = CalMongTheme.colors.neutral.foreground.default,
        )
    }
}
