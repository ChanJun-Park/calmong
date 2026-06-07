@file:Suppress("UnusedPrivateMember")

package com.jingom.calmong.core.designsystem.theme.shape

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

@Preview(name = "Shape tokens", showBackground = true)
@Composable
private fun ShapeTokensPreview() {
    CalMongTheme {
        val shapes = CalMongTheme.shapes

        Surface(color = CalMongTheme.colors.neutral.background.base) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ShapeRow(
                    label = "control",
                    shapes =
                        listOf(
                            shapes.control.compact,
                            shapes.control.default,
                            shapes.control.large,
                            shapes.control.pill,
                        ),
                )
                ShapeRow(
                    label = "surface",
                    shapes =
                        listOf(
                            shapes.surface.small,
                            shapes.surface.card,
                            shapes.surface.elevated,
                            shapes.surface.dialog,
                        ),
                )
                ShapeRow(
                    label = "content",
                    shapes =
                        listOf(
                            shapes.content.thumbnail,
                            shapes.content.image,
                            shapes.content.avatar,
                            shapes.content.badge,
                        ),
                )
            }
        }
    }
}

@Composable
private fun ShapeRow(
    label: String,
    shapes: List<Shape>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            color = CalMongTheme.colors.neutral.foreground.default,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            shapes.forEach { shape ->
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = shape,
                    color = CalMongTheme.colors.primary.background.subtle,
                ) {}
            }
        }
    }
}
