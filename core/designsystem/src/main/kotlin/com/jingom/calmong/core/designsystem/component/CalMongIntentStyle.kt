@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.StyleScope
import androidx.compose.foundation.style.disabled
import androidx.compose.foundation.style.focused
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.pressed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.jingom.calmong.core.designsystem.theme.CalMongTheme
import com.jingom.calmong.core.designsystem.theme.color.InteractionStates
import com.jingom.calmong.core.designsystem.theme.style.colors
import com.jingom.calmong.core.designsystem.theme.style.strokeWidths

/*
 * intent(의미) 축의 표면색·외곽선·상태 로직을 CalMongButton·CalMongIconButton 등
 * 모든 "채움형 인터랙터블"이 공유하는 단일 출처.
 *
 * 크기/모양(size·shape)은 컴포넌트마다 다르므로 각 레시피가 담당하고,
 * 여기서는 *의미색*과 *상호작용 상태*만 다룬다.
 */

/** intent 변형 — base 채움/콘텐츠 색 + (해당 시) 외곽선. hover/pressed는 [surfaceStates]가 stateLayer로. */
internal fun StyleScope.applyIntent(intent: CalMongButtonIntent) {
    when (intent) {
        CalMongButtonIntent.Primary ->
            surfaceStates(
                base = colors.primary.background.default,
                content = colors.primary.foreground.default,
                layer = colors.functional.stateLayer.solid,
            )
        CalMongButtonIntent.Secondary ->
            surfaceStates(
                base = colors.secondary.background.default,
                content = colors.secondary.foreground.default,
                layer = colors.functional.stateLayer.soft,
            )
        CalMongButtonIntent.Danger -> {
            surfaceStates(
                base = colors.functional.common.negative.subtle,
                content = colors.functional.common.negative.default,
                layer = colors.functional.stateLayer.soft,
            )
            borderColor(colors.functional.common.negative.default)
            borderWidth(strokeWidths.default)
        }
        CalMongButtonIntent.Neutral -> {
            surfaceStates(
                base = colors.neutral.background.raised2,
                content = colors.neutral.foreground.default,
                layer = colors.functional.stateLayer.soft,
            )
            borderColor(colors.neutral.stroke.subtle.default)
            borderWidth(strokeWidths.default)
        }
        CalMongButtonIntent.NeutralInverted ->
            surfaceStates(
                base = colors.neutral.background.inverted,
                content = colors.neutral.foreground.inverted,
                layer = colors.functional.stateLayer.solid,
            )
    }
}

/**
 * 채움 표면의 base 색 + 콘텐츠 색을 세팅하고, hover/pressed를 [layer](stateLayer soft/solid)를
 * base 위에 합성한 색으로 표현한다. 우리 디자인 시스템의 "상태 = 반투명 오버레이" 규칙을
 * Style의 단일 색 모델에 맞춰 compositeOver로 평탄화한 것.
 */
private fun StyleScope.surfaceStates(
    base: Color,
    content: Color,
    layer: InteractionStates,
) {
    background(base)
    contentColor(content)
    hovered { animate { background(layer.hover.compositeOver(base)) } }
    pressed { animate { background(layer.pressed.compositeOver(base)) } }
}

/** 모든 버튼 레시피가 공유하는 공통 상태 — 접근성 포커스 링 + 비활성 흐림. */
internal fun StyleScope.applyFocusAndDisabled() {
    // state: 접근성 포커스 링 — stroke 토큰 조합(색=color 시스템, 너비=stroke 시스템)
    focused {
        borderColor(colors.primary.stroke.default.default)
        borderWidth(strokeWidths.focus)
    }
    // state: 비활성
    disabled { animate { alpha(DISABLED_ALPHA) } }
}

/** 비활성 시 불투명도. 추후 motion/state 토큰 시스템이 생기면 그쪽으로 이전 대상. */
internal const val DISABLED_ALPHA = 0.38f

/**
 * intent별 콘텐츠(아이콘/텍스트) 색 — [applyIntent]가 Style 안에서 거는 contentColor와 **동일 매핑**.
 *
 * 텍스트는 `isInheritedTextStyleEnabled`로 Style의 contentColor를 자동 상속하지만,
 * `Icon`의 tint(`LocalContentColor`)는 Style의 contentColor를 따르지 않는다. 따라서
 * 아이콘 컴포넌트는 이 값을 tint로 **명시 전달**한다. 색 토큰은 [applyIntent]와 반드시 일치시킬 것.
 */
@Composable
@ReadOnlyComposable
internal fun calMongIntentContentColor(intent: CalMongButtonIntent): Color =
    when (intent) {
        CalMongButtonIntent.Primary -> CalMongTheme.colors.primary.foreground.default
        CalMongButtonIntent.Secondary -> CalMongTheme.colors.secondary.foreground.default
        CalMongButtonIntent.Danger -> CalMongTheme.colors.functional.common.negative.default
        CalMongButtonIntent.Neutral -> CalMongTheme.colors.neutral.foreground.default
        CalMongButtonIntent.NeutralInverted -> CalMongTheme.colors.neutral.foreground.inverted
    }
