package com.jingom.calmong.core.designsystem.theme.spacing

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

/**
 * CalMong semantic spacing system.
 *
 * 거리의 크기가 아니라 요소 사이 관계를 기준으로 선택한다.
 */
@Immutable
data class CalMongSpacings(
    val gap: GapSpacing,
    val inset: InsetSpacing,
    val section: SectionSpacing,
)

@Immutable
data class GapSpacing(
    /** 아이콘과 라벨처럼 매우 가까운 요소. */
    val tight: Dp,
    /** 한 그룹 안의 조밀한 요소. */
    val compact: Dp,
    /** 일반적인 형제 요소. */
    val default: Dp,
    /** 독립성이 조금 더 필요한 형제 요소. */
    val relaxed: Dp,
    /** 같은 컨테이너 안의 느슨한 그룹. */
    val loose: Dp,
)

@Immutable
data class InsetSpacing(
    /** 작은 control과 compact surface의 내부 여백. */
    val compact: Dp,
    /** 일반 Card와 화면 컨테이너의 내부 여백. */
    val default: Dp,
    /** 콘텐츠 밀도를 낮춘 편안한 내부 여백. */
    val comfortable: Dp,
    /** Dialog와 큰 surface의 내부 여백. */
    val spacious: Dp,
)

@Immutable
data class SectionSpacing(
    /** 직접 관련된 콘텐츠 그룹 사이. */
    val related: Dp,
    /** 일반적인 화면 section 사이. */
    val default: Dp,
    /** 의미가 뚜렷하게 다른 section 사이. */
    val distinct: Dp,
    /** 큰 page 영역이나 major block 사이. */
    val page: Dp,
)

internal val DefaultCalMongSpacings =
    CalMongSpacings(
        gap =
            GapSpacing(
                tight = CalMongSpacing.xs,
                compact = CalMongSpacing.sm,
                default = CalMongSpacing.md,
                relaxed = CalMongSpacing.lg,
                loose = CalMongSpacing.xxl,
            ),
        inset =
            InsetSpacing(
                compact = CalMongSpacing.sm,
                default = CalMongSpacing.lg,
                comfortable = CalMongSpacing.xl,
                spacious = CalMongSpacing.xxl,
            ),
        section =
            SectionSpacing(
                related = CalMongSpacing.xxl,
                default = CalMongSpacing.xxxl,
                distinct = CalMongSpacing.massive,
                page = CalMongSpacing.section,
            ),
    )

internal val LocalCalMongSpacings =
    staticCompositionLocalOf<CalMongSpacings> {
        error("CalMongSpacings가 제공되지 않았습니다. CalMongTheme { } 안에서 사용하세요.")
    }
