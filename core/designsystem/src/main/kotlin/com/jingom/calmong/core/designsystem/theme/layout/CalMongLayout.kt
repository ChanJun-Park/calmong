package com.jingom.calmong.core.designsystem.theme.layout

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Figma `layout.json`의 container 스케일에서 가져온 primitive 최대폭 subset.
 *
 * 제품 코드에서는 직접 쓰지 않고 [DefaultCalMongLayout]의 semantic 역할을 사용한다.
 */
internal object CalMongContainer {
    /** container/md */
    val md = 448.dp

    /** container/2xl */
    val xxl = 672.dp

    /** container/3xl */
    val xxxl = 768.dp
}

/**
 * CalMong semantic layout system.
 *
 * 반응형 골격 분기는 [WindowWidthClass], 콘텐츠 최대폭은 [CalMongLayout.contentMaxWidth]가 담당한다.
 * 요소 간격/내부 여백은 layout이 아니라 `CalMongTheme.spacings`를 쓴다(역할 중복 방지).
 * `CalMongTheme.layout.contentMaxWidth.form`처럼 접근한다.
 */
@Immutable
data class CalMongLayout(
    val contentMaxWidth: ContentMaxWidth,
)

/**
 * 넓은 화면에서 콘텐츠가 끝까지 늘어지지 않도록 제한하는 최대폭. `Modifier.widthIn(max = …)`에 쓴다.
 */
@Immutable
data class ContentMaxWidth(
    /** 단일 컬럼 폼·다이얼로그(일정 입력 등). */
    val form: Dp,
    /** 읽는 텍스트(다이어리·설정 설명). */
    val prose: Dp,
    /** 태블릿에서 중앙 정렬되는 본문 컨테이너. */
    val wide: Dp,
)

internal val DefaultCalMongLayout =
    CalMongLayout(
        contentMaxWidth =
            ContentMaxWidth(
                form = CalMongContainer.md,
                prose = CalMongContainer.xxl,
                wide = CalMongContainer.xxxl,
            ),
    )

internal val LocalCalMongLayout =
    staticCompositionLocalOf<CalMongLayout> {
        error("CalMongLayout가 제공되지 않았습니다. CalMongTheme { } 안에서 사용하세요.")
    }
