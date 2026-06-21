@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.theme.style

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.pressed
import androidx.compose.ui.graphics.TransformOrigin

/**
 * 모든 인터랙션 가능한 요소가 공유하는 "물리적 눌림" 효과 (atomic Style).
 *
 * 눌렀을 때 중심 기준으로 살짝 축소되며 스프링으로 되돌아온다 — 실제로 눌리는 듯한 촉감.
 * 색·패딩 같은 시각 속성은 건드리지 않고 *변형(scale)만* 담당하므로, 어떤 컴포넌트의
 * Style이든 `then CalMongPressEffect`로 합성해 재사용한다(웹의 atomic 유틸리티 + 합성 패턴).
 *
 * ```kotlin
 * Modifier.styleable(styleState, calMongButtonStyle(intent, size) then CalMongPressEffect then style)
 * ```
 *
 * 같은 `StyleState`(interactionSource)를 공유하므로 Button·Card·Chip 등 어디에 붙여도 동일하게 동작한다.
 */
val CalMongPressEffect: Style =
    Style {
        transformOrigin(TransformOrigin.Center)
        pressed {
            animate(
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            ) {
                scale(PRESSED_SCALE)
            }
        }
    }

/** 눌림 시 축소 비율. 추후 motion 토큰 시스템이 생기면 그쪽으로 이전 대상. */
private const val PRESSED_SCALE = 0.96f
