package com.jingom.calmong.core.designsystem.theme.stroke

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

/**
 * CalMong semantic stroke(외곽선 너비) system.
 *
 * 외곽선의 *색*은 color 시스템(`CalMongTheme.colors.*.stroke`)이, *너비*는 이 시스템이 담당한다.
 * 두께 수치(`1.dp`)를 직접 쓰지 않고 역할(`CalMongTheme.strokeWidths.emphasis`)로 선택한다.
 */
@Immutable
data class CalMongStrokeWidths(
    /** 테두리 없음. 상태에 따라 외곽선을 조건부로 제거할 때. */
    val none: Dp,
    /** 기본 외곽선·구분선 (입력/카드/컨테이너 경계). */
    val default: Dp,
    /** 강조·선택 외곽선. 주의를 끄는 표면(토스트 등)과 selected 상태. */
    val emphasis: Dp,
    /** 포커스 링. 가장 또렷한 접근성 표시. */
    val focus: Dp,
)

internal val DefaultCalMongStrokeWidths =
    CalMongStrokeWidths(
        none = CalMongStrokeWidth.none,
        default = CalMongStrokeWidth.hairline,
        emphasis = CalMongStrokeWidth.thin,
        focus = CalMongStrokeWidth.thick,
    )

internal val LocalCalMongStrokeWidths =
    staticCompositionLocalOf<CalMongStrokeWidths> {
        error("CalMongStrokeWidths가 제공되지 않았습니다. CalMongTheme { } 안에서 사용하세요.")
    }
