package com.jingom.calmong.core.designsystem.theme.elevation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import com.jingom.calmong.core.designsystem.theme.color.CalMongColorScheme

/**
 * CalMong semantic elevation system.
 *
 * 각 역할은 surface 색과 shadow를 함께 제공한다. Light는 shadow, Dark는 raised surface 색을
 * elevation의 주 표현으로 사용한다.
 */
@Immutable
data class CalMongElevations(
    val flat: ElevationStyle,
    val raised1: ElevationStyle,
    val raised2: ElevationStyle,
    val overlay: ElevationStyle,
    val modal: ElevationStyle,
)

@Immutable
data class ElevationStyle(
    val surface: Color,
    val shadows: List<Shadow>,
)

internal fun lightCalMongElevations(colors: CalMongColorScheme) =
    CalMongElevations(
        flat =
            ElevationStyle(
                surface = colors.neutral.background.default,
                shadows = CalMongShadows.none,
            ),
        raised1 =
            ElevationStyle(
                surface = colors.neutral.background.default,
                shadows = CalMongShadows.sm,
            ),
        raised2 =
            ElevationStyle(
                surface = colors.neutral.background.default,
                shadows = CalMongShadows.base,
            ),
        overlay =
            ElevationStyle(
                surface = colors.neutral.background.default,
                shadows = CalMongShadows.lg,
            ),
        modal =
            ElevationStyle(
                surface = colors.neutral.background.default,
                shadows = CalMongShadows.xxl,
            ),
    )

internal fun darkCalMongElevations(colors: CalMongColorScheme) =
    CalMongElevations(
        flat =
            ElevationStyle(
                surface = colors.neutral.background.default,
                shadows = CalMongShadows.none,
            ),
        raised1 =
            ElevationStyle(
                surface = colors.neutral.background.raised1,
                shadows = CalMongShadows.none,
            ),
        raised2 =
            ElevationStyle(
                surface = colors.neutral.background.raised2,
                shadows = CalMongShadows.none,
            ),
        overlay =
            ElevationStyle(
                surface = colors.neutral.background.raised2,
                shadows = CalMongShadows.sm,
            ),
        modal =
            ElevationStyle(
                surface = colors.neutral.background.raised2,
                shadows = CalMongShadows.xs,
            ),
    )

/** [ElevationStyle]의 다중 drop shadow를 같은 [shape]에 순서대로 적용한다. */
fun Modifier.elevationShadow(
    elevation: ElevationStyle,
    shape: Shape,
): Modifier =
    elevation.shadows.fold(this) { modifier, shadow ->
        modifier.dropShadow(shape, shadow)
    }

internal val LocalCalMongElevations =
    staticCompositionLocalOf<CalMongElevations> {
        error("CalMongElevations가 제공되지 않았습니다. CalMongTheme { } 안에서 사용하세요.")
    }
