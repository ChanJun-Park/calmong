@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
 * [CalMongIconButton]의 시각 회귀(screenshot) 테스트.
 *
 * intent 5종(rest)을 light/dark로 고정한다 — 원형(pill) 모양·intent별 채움/외곽선·아이콘 tint 회귀 방지.
 * golden 갱신: `./gradlew :core:designsystem:recordPaparazziDebug`
 */
class CalMongIconButtonScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            deviceConfig = DeviceConfig.PIXEL_5,
            renderingMode = RenderingMode.SHRINK,
        )

    @Test
    fun gallery_light() {
        paparazzi.snapshot { Gallery(darkTheme = false) }
    }

    @Test
    fun gallery_dark() {
        paparazzi.snapshot { Gallery(darkTheme = true) }
    }

    @Composable
    private fun Gallery(darkTheme: Boolean) {
        ComposeFoundationFlags.isInheritedTextStyleEnabled = true
        CalMongTheme(darkTheme = darkTheme) {
            Row(
                modifier =
                    Modifier
                        .background(CalMongTheme.colors.neutral.background.base)
                        .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CalMongButtonIntent.entries.forEach { intent ->
                    CalMongIconButton(
                        onClick = {},
                        imageVector = Icons.Filled.Add,
                        contentDescription = "추가",
                        intent = intent,
                    )
                }
            }
        }
    }
}
