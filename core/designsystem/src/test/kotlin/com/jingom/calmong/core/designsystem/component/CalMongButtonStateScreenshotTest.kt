@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams.RenderingMode
import com.jingom.calmong.core.designsystem.theme.CalMongTheme
import com.jingom.calmong.core.designsystem.theme.style.CalMongPressEffect
import org.junit.Rule
import org.junit.Test

/**
 * [CalMongButton] 레시피의 *상호작용 상태* 시각 회귀(screenshot) 테스트.
 *
 * Style API의 핵심 가치인 상태 표현(hover/pressed/focused/disabled)을 golden으로 고정한다.
 * 정적 렌더라 interactionSource 비동기 수집 대신 [MutableStyleState]의 상태 플래그를 직접 세팅한다
 * (docs/compose-styles/03-state-animations.md의 MediaPlayer 패턴). 플래그가 컴포지션 시점에 확정되므로
 * `animate { }`가 목표값에서 시작 → settle된 상태가 결정적으로 렌더된다.
 *
 * 행 = 상태(Rest/Hovered/Pressed/Focused/Disabled), 열 = 대표 intent.
 * golden 갱신: `./gradlew :core:designsystem:recordPaparazziDebug`
 */
class CalMongButtonStateScreenshotTest {
    @get:Rule
    val paparazzi =
        Paparazzi(
            // 라벨 + intent 3열이 들어가도록 캔버스 폭을 넓힌다(SHRINK가 콘텐츠에 맞춰 자름).
            deviceConfig = DeviceConfig.PIXEL_5.copy(screenWidth = 1600, screenHeight = 2200),
            renderingMode = RenderingMode.SHRINK,
        )

    @Test
    fun states_light() {
        paparazzi.snapshot { StateMatrix(darkTheme = false) }
    }

    @Test
    fun states_dark() {
        paparazzi.snapshot { StateMatrix(darkTheme = true) }
    }

    @Composable
    private fun StateMatrix(darkTheme: Boolean) {
        ComposeFoundationFlags.isInheritedTextStyleEnabled = true
        CalMongTheme(darkTheme = darkTheme) {
            Column(
                modifier =
                    Modifier
                        .background(CalMongTheme.colors.neutral.background.base)
                        .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                STATES.forEach { (label, configure) ->
                    StateRow(label = label, configure = configure)
                }
            }
        }
    }

    @Composable
    private fun StateRow(
        label: String,
        configure: MutableStyleState.() -> Unit,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = CalMongTheme.colors.neutral.foreground.subtle,
                modifier = Modifier.width(88.dp),
            )
            COLUMNS.forEach { (text, intent) ->
                StateChip(text = text, intent = intent, configure = configure)
            }
        }
    }

    /** 실제 [calMongButtonStyle] 레시피 + [CalMongPressEffect]를, 강제 세팅한 상태로 렌더하는 칩. */
    @Composable
    private fun StateChip(
        text: String,
        intent: CalMongButtonIntent,
        configure: MutableStyleState.() -> Unit,
    ) {
        val state = remember { MutableStyleState(MutableInteractionSource()) }
        // 매 컴포지션마다 기본값으로 리셋 후 해당 상태만 켠다 (write-before-read).
        state.apply {
            isEnabled = true
            isHovered = false
            isPressed = false
            isFocused = false
            configure()
        }
        Box(
            modifier = Modifier.styleable(state, calMongButtonStyle(intent) then CalMongPressEffect),
            contentAlignment = Alignment.Center,
        ) {
            Text(text)
        }
    }

    private companion object {
        val STATES: List<Pair<String, MutableStyleState.() -> Unit>> =
            listOf(
                "Rest" to {},
                "Hovered" to { isHovered = true },
                "Pressed" to { isPressed = true },
                "Focused" to { isFocused = true },
                "Disabled" to { isEnabled = false },
            )

        val COLUMNS: List<Pair<String, CalMongButtonIntent>> =
            listOf(
                "Primary" to CalMongButtonIntent.Primary,
                "Neutral" to CalMongButtonIntent.Neutral,
                "Inverted" to CalMongButtonIntent.NeutralInverted,
            )
    }
}
