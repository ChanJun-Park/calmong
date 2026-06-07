package com.jingom.calmong.core.designsystem.theme.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape

/**
 * CalMong semantic shape system.
 *
 * 크기 대신 역할로 선택한다.
 * `CalMongTheme.shapes.surface.card`처럼 접근한다.
 */
@Immutable
data class CalMongShapes(
    val control: ControlShapes,
    val surface: SurfaceShapes,
    val content: ContentShapes,
)

@Immutable
data class ControlShapes(
    /** 작은 입력과 compact control. */
    val compact: Shape,
    /** 일반 버튼과 입력 필드. */
    val default: Shape,
    /** 큰 CTA와 검색창. */
    val large: Shape,
    /** Chip과 segmented control. */
    val pill: Shape,
)

@Immutable
data class SurfaceShapes(
    /** Tooltip과 작은 popup. */
    val small: Shape,
    /** 일반 Card. */
    val card: Shape,
    /** Menu와 elevated Card. */
    val elevated: Shape,
    /** 위쪽 모서리만 둥근 Bottom Sheet. */
    val sheet: Shape,
    /** Dialog와 큰 modal surface. */
    val dialog: Shape,
)

@Immutable
data class ContentShapes(
    /** 작은 thumbnail. */
    val thumbnail: Shape,
    /** 일반 콘텐츠 이미지. */
    val image: Shape,
    /** 원형 avatar와 pet portrait. */
    val avatar: Shape,
    /** Capsule badge. */
    val badge: Shape,
)

internal val DefaultCalMongShapes =
    CalMongShapes(
        control =
            ControlShapes(
                compact = RoundedCornerShape(CalMongRadius.md),
                default = RoundedCornerShape(CalMongRadius.lg),
                large = RoundedCornerShape(CalMongRadius.xl),
                pill = RoundedCornerShape(CalMongRadius.full),
            ),
        surface =
            SurfaceShapes(
                small = RoundedCornerShape(CalMongRadius.lg),
                card = RoundedCornerShape(CalMongRadius.xl),
                elevated = RoundedCornerShape(CalMongRadius.xxl),
                sheet =
                    RoundedCornerShape(
                        topStart = CalMongRadius.xxxl,
                        topEnd = CalMongRadius.xxxl,
                    ),
                dialog = RoundedCornerShape(CalMongRadius.xxxl),
            ),
        content =
            ContentShapes(
                thumbnail = RoundedCornerShape(CalMongRadius.lg),
                image = RoundedCornerShape(CalMongRadius.xl),
                avatar = RoundedCornerShape(CalMongRadius.full),
                badge = RoundedCornerShape(CalMongRadius.full),
            ),
    )

/**
 * Material3 component shape scale.
 *
 * Material 역할은 semantic component 역할보다 범용적이므로 primitive scale에 직접 고정한다.
 */
internal fun calMongMaterialShapes(): Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(CalMongRadius.base),
        small = RoundedCornerShape(CalMongRadius.lg),
        medium = RoundedCornerShape(CalMongRadius.xl),
        large = RoundedCornerShape(CalMongRadius.xxl),
        extraLarge = RoundedCornerShape(CalMongRadius.xxxl),
    )

internal val LocalCalMongShapes =
    staticCompositionLocalOf<CalMongShapes> {
        error("CalMongShapes가 제공되지 않았습니다. CalMongTheme { } 안에서 사용하세요.")
    }
