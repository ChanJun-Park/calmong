package com.jingom.calmong.core.designsystem.theme.layout

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 창 너비 분기 임계값(Android dp).
 *
 * Tailwind의 웹 breakpoint(px) 대신 Material WindowSizeClass 기준 dp를 쓴다.
 * Figma `layout.json`의 breakpoint 변수도 이 값에 맞춘다.
 * 화면/컴포넌트는 직접 쓰지 말고 [WindowWidthClass]로 분기한다.
 */
internal object CalMongBreakpoints {
    /** compact 상한 — 이 미만은 Compact. */
    val medium = 600.dp
    val expanded = 840.dp
    val large = 1200.dp
}

/**
 * 창 너비 역할 분류. 화면 골격(단일/2-pane/list-detail) 선택 기준.
 *
 * - [Compact] `<600dp` : 폰 세로 — 단일 페인 + Bottom bar
 * - [Medium] `600–839dp` : 폰 가로·소형 폴더블·태블릿 세로 — 2-pane + Nav rail
 * - [Expanded] `840–1199dp` : 태블릿·대형 폴더블 — list-detail + Nav rail/Drawer
 * - [Large] `≥1200dp` : 대형 태블릿·데스크톱 — list-detail(여유 폭)
 */
enum class WindowWidthClass {
    Compact,
    Medium,
    Expanded,
    Large,
    ;

    companion object {
        /** 현재 창 너비([width])를 역할로 분류. */
        fun fromWidth(width: Dp): WindowWidthClass =
            when {
                width < CalMongBreakpoints.medium -> Compact
                width < CalMongBreakpoints.expanded -> Medium
                width < CalMongBreakpoints.large -> Expanded
                else -> Large
            }
    }
}

/**
 * 현재 창 너비 클래스. 런타임 창 크기에 의존하므로 색/간격과 달리 앱 루트에서 산출해 제공한다.
 * 제공되지 않으면 폰(Compact)으로 가정한다.
 */
internal val LocalWindowWidthClass = staticCompositionLocalOf { WindowWidthClass.Compact }
