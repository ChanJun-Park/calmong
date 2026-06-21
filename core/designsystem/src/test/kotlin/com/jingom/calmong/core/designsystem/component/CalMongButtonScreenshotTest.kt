@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import com.jingom.calmong.core.designsystem.theme.CalMongTheme
import org.junit.Rule
import org.junit.Test

/**
 * [CalMongButton]의 시각 회귀(screenshot) 테스트.
 *
 * Style API로 만든 첫 컴포넌트라 intent/size/state 매트릭스의 *생김새*를 golden 이미지로 고정한다.
 * 컴파일로는 못 잡는 색·여백·눌림/포커스 표현 회귀를 잡는 게 목적.
 * golden 갱신: `./gradlew :core:designsystem:recordPaparazziDebug`
 * 검증: `./gradlew :core:designsystem:verifyPaparazziDebug`
 */
class CalMongButtonScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            renderingMode = RenderingMode.SHRINK,
        )

    @Test
    fun gallery_light() {
        paparazzi.snapshot { ButtonGallery(darkTheme = false) }
    }

    @Test
    fun gallery_dark() {
        paparazzi.snapshot { ButtonGallery(darkTheme = true) }
    }

    @Composable
    private fun ButtonGallery(darkTheme: Boolean) {
        // contentColor가 자식 Text로 상속되도록 opt-in (앱 진입점에서 1회 설정하는 것과 동일)
        ComposeFoundationFlags.isInheritedTextStyleEnabled = true
        CalMongTheme(darkTheme = darkTheme) {
            Column(
                modifier =
                    Modifier
                        .background(CalMongTheme.colors.neutral.background.base)
                        .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ButtonRow {
                    CalMongButton(onClick = {}, intent = CalMongButtonIntent.Primary) { Text("확인") }
                    CalMongButton(onClick = {}, intent = CalMongButtonIntent.Secondary) { Text("취소") }
                    CalMongButton(onClick = {}, intent = CalMongButtonIntent.Danger) { Text("삭제") }
                }
                ButtonRow {
                    CalMongButton(onClick = {}, intent = CalMongButtonIntent.Neutral) { Text("Neutral") }
                    CalMongButton(onClick = {}, intent = CalMongButtonIntent.NeutralInverted) { Text("Inverted") }
                }
                ButtonRow {
                    CalMongButton(onClick = {}, size = CalMongButtonSize.Sm) { Text("Small") }
                    CalMongButton(onClick = {}, size = CalMongButtonSize.Md) { Text("Medium") }
                    CalMongButton(onClick = {}, size = CalMongButtonSize.Lg) { Text("Large") }
                }
                CalMongButton(onClick = {}, enabled = false) { Text("비활성") }
            }
        }
    }

    @Composable
    private fun ButtonRow(content: @Composable () -> Unit) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = { content() },
        )
    }
}
