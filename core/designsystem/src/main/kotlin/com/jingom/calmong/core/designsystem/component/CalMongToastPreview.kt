@file:Suppress("UnusedPrivateMember") // @Preview 함수는 IDE/툴이 호출 — detekt가 미사용으로 오인

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

@Preview(name = "Toast · Light", showBackground = true)
@Composable
private fun CalMongToastLightPreview() {
    CalMongTheme(darkTheme = false) { ToastShowcase() }
}

@Preview(name = "Toast · Dark", showBackground = true)
@Composable
private fun CalMongToastDarkPreview() {
    CalMongTheme(darkTheme = true) { ToastShowcase() }
}

@Composable
private fun ToastShowcase() {
    Column(
        modifier =
            Modifier
                .background(CalMongTheme.colors.neutral.background.base)
                .padding(CalMongTheme.spacings.inset.default),
        verticalArrangement = Arrangement.spacedBy(CalMongTheme.spacings.gap.default),
    ) {
        CalMongToast(message = "저장되었습니다", style = CalMongToastStyle.Info)
        CalMongToast(message = "저장에 실패했습니다", style = CalMongToastStyle.Error)
        CalMongToast(message = "네트워크가 불안정합니다", style = CalMongToastStyle.Warning)
    }
}
