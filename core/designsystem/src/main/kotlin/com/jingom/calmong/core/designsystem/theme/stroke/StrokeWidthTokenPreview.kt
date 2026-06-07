@file:Suppress("UnusedPrivateMember") // @Preview 함수는 IDE/툴이 호출 — detekt가 미사용으로 오인

package com.jingom.calmong.core.designsystem.theme.stroke

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

@Preview(name = "Stroke width tokens", showBackground = true)
@Composable
private fun StrokeWidthTokensPreview() {
    CalMongTheme {
        val widths = CalMongTheme.strokeWidths
        Surface(color = CalMongTheme.colors.neutral.background.base) {
            Column(
                modifier = Modifier.padding(CalMongTheme.spacings.inset.default),
                verticalArrangement = Arrangement.spacedBy(CalMongTheme.spacings.gap.default),
            ) {
                StrokeSample("default (1)", widths.default)
                StrokeSample("emphasis (2)", widths.emphasis)
                StrokeSample("focus (4)", widths.focus)
            }
        }
    }
}

@Composable
private fun StrokeSample(
    label: String,
    width: Dp,
) {
    Box(
        modifier =
            Modifier
                .background(CalMongTheme.colors.neutral.background.default, CalMongTheme.shapes.surface.small)
                .border(width, CalMongTheme.colors.primary.stroke.default.default, CalMongTheme.shapes.surface.small)
                .padding(CalMongTheme.spacings.inset.default),
    ) {
        Text(text = label, color = CalMongTheme.colors.neutral.foreground.default)
    }
}
