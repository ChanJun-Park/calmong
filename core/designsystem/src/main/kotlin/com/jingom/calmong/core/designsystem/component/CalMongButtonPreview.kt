@file:Suppress("UnusedPrivateMember")
@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.CalMongTheme

@Preview(name = "CalMongButton - Light", showBackground = true)
@Preview(name = "CalMongButton - Dark", showBackground = true, uiMode = 0x20)
@Composable
private fun CalMongButtonPreview() {
    // 실험 기능: contentColor가 자식 Text로 상속되도록 opt-in (앱 진입점에서 1회 설정 대상)
    ComposeFoundationFlags.isInheritedTextStyleEnabled = true
    CalMongTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 강조 intent (Md)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CalMongButton(onClick = {}, intent = CalMongButtonIntent.Primary) { Text("확인") }
                CalMongButton(onClick = {}, intent = CalMongButtonIntent.Secondary) { Text("취소") }
                CalMongButton(onClick = {}, intent = CalMongButtonIntent.Danger) { Text("삭제") }
            }
            // 중립 intent — 일상적 행동(남발 방지)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CalMongButton(onClick = {}, intent = CalMongButtonIntent.Neutral) { Text("Neutral") }
                CalMongButton(onClick = {}, intent = CalMongButtonIntent.NeutralInverted) { Text("Inverted") }
            }
            // size 3종 (Primary)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CalMongButton(onClick = {}, size = CalMongButtonSize.Sm) { Text("Small") }
                CalMongButton(onClick = {}, size = CalMongButtonSize.Md) { Text("Medium") }
                CalMongButton(onClick = {}, size = CalMongButtonSize.Lg) { Text("Large") }
            }
            // 비활성
            CalMongButton(onClick = {}, enabled = false) { Text("비활성") }
        }
    }
}
