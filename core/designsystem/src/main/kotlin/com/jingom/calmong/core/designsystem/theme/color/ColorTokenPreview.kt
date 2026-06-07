@file:Suppress("UnusedPrivateMember") // @Preview 함수는 IDE/툴이 호출 — detekt가 미사용으로 오인

package com.jingom.calmong.core.designsystem.theme.color

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

/**
 * semantic 색상 토큰 시각 확인용 Preview. 실제 화면에서 쓰는 컴포넌트가 아니다.
 */
@Preview(name = "Color tokens · Light", showBackground = true)
@Composable
private fun ColorTokensLightPreview() {
    CalMongTheme(darkTheme = false) { ColorTokenBoard() }
}

@Preview(name = "Color tokens · Dark", showBackground = true)
@Composable
private fun ColorTokensDarkPreview() {
    CalMongTheme(darkTheme = true) { ColorTokenBoard() }
}

@Composable
private fun ColorTokenBoard() {
    val c = CalMongTheme.colors
    Surface(color = c.neutral.background.base) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SwatchRow(
                "primary",
                listOf(
                    c.primary.background.subtle,
                    c.primary.background.default,
                    c.primary.background.bold,
                    c.primary.foreground.default,
                ),
            )
            SwatchRow(
                "secondary",
                listOf(
                    c.secondary.background.subtle,
                    c.secondary.background.default,
                    c.secondary.background.bold,
                    c.secondary.foreground.default,
                ),
            )
            SwatchRow(
                "neutral.bg",
                listOf(
                    c.neutral.background.base,
                    c.neutral.background.default,
                    c.neutral.background.raised2,
                    c.neutral.background.inverted,
                ),
            )
            SwatchRow(
                "functional",
                listOf(
                    c.functional.common.positive.default,
                    c.functional.common.negative.default,
                    c.functional.common.informative.default,
                    c.functional.common.attention.default,
                ),
            )
            SwatchRow(
                "specific",
                listOf(
                    c.functional.specific.like.default,
                    c.functional.specific.link.default,
                    c.functional.general.highlight,
                    c.functional.general.disabled,
                ),
            )
            StateRow("stroke.default", c.neutral.stroke.default)
            StateRow("stateLayer.soft", c.functional.stateLayer.soft)
            StateRow("stateLayer.solid", c.functional.stateLayer.solid)
        }
    }
}

/** InteractionStates(6개 상태)를 default→disabled 순으로 나열. */
@Composable
private fun StateRow(
    label: String,
    states: InteractionStates,
) = SwatchRow(
    label,
    listOf(
        states.default,
        states.hover,
        states.focused,
        states.pressed,
        states.activated,
        states.disabled,
    ),
)

@Composable
private fun SwatchRow(
    label: String,
    colors: List<Color>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, color = CalMongTheme.colors.neutral.foreground.default)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            colors.forEach { color ->
                Surface(
                    modifier = Modifier.size(56.dp),
                    color = color,
                    shape = CalMongTheme.shapes.control.default,
                    border =
                        BorderStroke(
                            width = 1.dp,
                            color = CalMongTheme.colors.neutral.stroke.divider.default,
                        ),
                ) {}
            }
        }
    }
}
