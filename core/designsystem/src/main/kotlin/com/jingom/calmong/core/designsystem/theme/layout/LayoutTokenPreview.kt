@file:Suppress("UnusedPrivateMember") // @Preview 함수는 IDE/툴이 호출 — detekt가 미사용으로 오인

package com.jingom.calmong.core.designsystem.theme.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

/**
 * semantic layout 토큰 시각 확인용 Preview. 실제 화면 컴포넌트가 아니다.
 */
@Preview(name = "Layout tokens", showBackground = true, widthDp = 820)
@Composable
private fun LayoutTokensPreview() {
    CalMongTheme {
        val l = CalMongTheme.layout
        Surface(color = CalMongTheme.colors.neutral.background.base) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "WindowWidthClass: Compact<600 · Medium 600–839 · Expanded 840–1199 · Large≥1200",
                    color = CalMongTheme.colors.neutral.foreground.subtle,
                )
                MaxWidthBar("contentMaxWidth.form (448)", l.contentMaxWidth.form)
                MaxWidthBar("contentMaxWidth.prose (672)", l.contentMaxWidth.prose)
                MaxWidthBar("contentMaxWidth.wide (768)", l.contentMaxWidth.wide)
            }
        }
    }
}

@Composable
private fun MaxWidthBar(
    label: String,
    maxWidth: Dp,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, color = CalMongTheme.colors.neutral.foreground.default)
        Box(
            modifier =
                Modifier
                    .width(maxWidth)
                    .background(CalMongTheme.colors.primary.background.subtle, CalMongTheme.shapes.control.compact)
                    .padding(8.dp),
        ) {
            Text(text = "max $maxWidth", color = CalMongTheme.colors.neutral.foreground.default)
        }
    }
}
