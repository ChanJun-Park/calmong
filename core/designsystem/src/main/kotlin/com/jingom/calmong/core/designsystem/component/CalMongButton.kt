@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleScope
import androidx.compose.foundation.style.disabled
import androidx.compose.foundation.style.focused
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.pressed
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jingom.calmong.core.designsystem.theme.color.InteractionStates
import com.jingom.calmong.core.designsystem.theme.style.CalMongPressEffect
import com.jingom.calmong.core.designsystem.theme.style.colors
import com.jingom.calmong.core.designsystem.theme.style.shapes
import com.jingom.calmong.core.designsystem.theme.style.spacings
import com.jingom.calmong.core.designsystem.theme.style.strokeWidths

/**
 * 앱 공통 버튼 — calmong의 첫 Style API 기반 컴포넌트(PoC).
 *
 * 시각은 [calMongButtonStyle] 레시피(변형 = intent × size)가, 동작(클릭/접근성)은 modifier가 담당한다
 * (Style API 철학: 시각=Style, 동작=modifier). 눌림은 모든 인터랙터블이 공유하는 [CalMongPressEffect]를
 * `then` 합성해 재현하고, hover/pressed 색은 stateLayer 오버레이를, focus는 stroke 토큰을 쓴다.
 *
 * @param style 호출부가 추가로 덮어쓸 Style. 레시피·press 효과 *뒤에* 합성되어 last-write-wins로 우선한다.
 */
@Composable
fun CalMongButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    intent: CalMongButtonIntent = CalMongButtonIntent.Primary,
    size: CalMongButtonSize = CalMongButtonSize.Md,
    enabled: Boolean = true,
    style: Style = Style,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource) { it.isEnabled = enabled }
    Row(
        modifier =
            modifier
                .semantics { role = Role.Button }
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ).styleable(styleState, calMongButtonStyle(intent, size) then CalMongPressEffect then style),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** 버튼 의미(intent) 변형 축. */
enum class CalMongButtonIntent {
    /** 핵심 행동 — 브랜드 채움(Indigo). 화면당 1개 권장. */
    Primary,

    /** 보조 강조 — 브랜드 보조 채움(Amber). */
    Secondary,

    /** 파괴적 행동 — negative 의미색. */
    Danger,

    /** 일상적/중립 행동 — 회색 표면. primary/secondary 남발을 막는 기본값. */
    Neutral,

    /** 짙은 역상 표면 위 중립 행동. */
    NeutralInverted,
}

/** 버튼 크기 변형 축. */
enum class CalMongButtonSize { Sm, Md, Lg }

/**
 * 버튼 레시피 — 웹의 CVA(class-variance-authority)처럼 변형 축 조합을 Style로 만든다.
 * 토큰은 [colors]/[shapes]/[spacings]/[strokeWidths] StyleScope 확장으로 읽으므로 라이트/다크가 자동 반영된다.
 */
fun calMongButtonStyle(
    intent: CalMongButtonIntent = CalMongButtonIntent.Primary,
    size: CalMongButtonSize = CalMongButtonSize.Md,
): Style =
    Style {
        shape(shapes.surface.small)
        applyIntent(intent)
        applySize(size)
        fontWeight(FontWeight.Medium)

        // state: 접근성 포커스 링 — stroke 토큰 조합(색=color 시스템, 너비=stroke 시스템)
        focused {
            borderColor(colors.primary.stroke.default.default)
            borderWidth(strokeWidths.focus)
        }
        // state: 비활성
        disabled { animate { alpha(DISABLED_ALPHA) } }
    }

/** intent 변형 — base 채움/콘텐츠 색 + (해당 시) 외곽선. hover/pressed는 [surfaceStates]가 stateLayer로. */
private fun StyleScope.applyIntent(intent: CalMongButtonIntent) {
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

/** size 변형 — 안쪽 여백(inset 토큰) + 글자 크기. */
private fun StyleScope.applySize(size: CalMongButtonSize) {
    when (size) {
        CalMongButtonSize.Sm -> {
            contentPadding(horizontal = spacings.inset.default, vertical = spacings.inset.compact)
            fontSize(14.sp)
        }
        CalMongButtonSize.Md -> {
            contentPadding(horizontal = spacings.inset.comfortable, vertical = spacings.inset.default)
            fontSize(16.sp)
        }
        CalMongButtonSize.Lg -> {
            contentPadding(horizontal = spacings.inset.spacious, vertical = spacings.inset.comfortable)
            fontSize(18.sp)
        }
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

private const val DISABLED_ALPHA = 0.38f
