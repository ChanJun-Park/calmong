@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.jingom.calmong.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.rememberUpdatedStyleState
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jingom.calmong.core.designsystem.theme.style.CalMongPressEffect
import com.jingom.calmong.core.designsystem.theme.style.shapes

/**
 * 원형 아이콘 버튼 — 아이콘 하나만 담는 콤팩트한 액션(추가/닫기/즐겨찾기 등).
 *
 * 의미색·상호작용 상태(hover/pressed/focus/disabled)는 [CalMongButton]과 같은 공유 로직
 * ([applyIntent]·[applyFocusAndDisabled])을 쓰고, 모양만 [shapes].control.pill(완전한 원)로 바꾼다.
 * 눌림은 모든 인터랙터블이 공유하는 [CalMongPressEffect]를 `then` 합성해 재현한다.
 *
 * 기본값 intent는 [CalMongButtonIntent.Neutral] — 아이콘 버튼은 대개 유틸리티 액션이라
 * primary/secondary 남발을 막는다.
 *
 * @param contentDescription 접근성 레이블. 장식용이면 null.
 * @param style 호출부가 추가로 덮어쓸 Style. 레시피·press 효과 *뒤에* 합성되어 last-write-wins로 우선한다.
 */
@Composable
fun CalMongIconButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    intent: CalMongButtonIntent = CalMongButtonIntent.Neutral,
    enabled: Boolean = true,
    style: Style = Style,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = rememberUpdatedStyleState(interactionSource) { it.isEnabled = enabled }
    Box(
        modifier =
            modifier
                .semantics { role = Role.Button }
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ).styleable(styleState, calMongIconButtonStyle(intent) then CalMongPressEffect then style),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(IconSize),
            // Icon tint는 Style의 contentColor를 자동으로 따르지 않아 intent 색을 명시 전달한다.
            tint = calMongIntentContentColor(intent),
        )
    }
}

/**
 * 아이콘 버튼 레시피 — 모양은 원(pill), 색·상태는 [CalMongButton]과 공유.
 * 토큰은 StyleScope 확장으로 읽으므로 라이트/다크가 자동 반영된다.
 */
fun calMongIconButtonStyle(intent: CalMongButtonIntent = CalMongButtonIntent.Neutral): Style =
    Style {
        shape(shapes.control.pill)
        applyIntent(intent)
        size(IconButtonSize)
        applyFocusAndDisabled()
    }

// 크기 토큰 시스템(아이콘 크기·터치 영역)이 아직 없어 컴포넌트 로컬 상수로 둔다.
// 향후 size/layout 토큰 시스템이 생기면 그쪽으로 이전한다(SPACING_SYSTEM.md의 "크기 토큰과의 경계" 참고).
// 터치 영역 40dp는 권장 48dp보다 작다 — 정식 보장은 size 시스템에서 처리.
private val IconButtonSize = 40.dp
private val IconSize = 24.dp
