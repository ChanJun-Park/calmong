package com.jingom.calmong.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import com.jingom.calmong.core.designsystem.theme.color.CalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.LocalCalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.darkCalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.color.lightCalMongColorScheme
import com.jingom.calmong.core.designsystem.theme.elevation.CalMongElevations
import com.jingom.calmong.core.designsystem.theme.elevation.LocalCalMongElevations
import com.jingom.calmong.core.designsystem.theme.elevation.darkCalMongElevations
import com.jingom.calmong.core.designsystem.theme.elevation.lightCalMongElevations
import com.jingom.calmong.core.designsystem.theme.layout.CalMongLayout
import com.jingom.calmong.core.designsystem.theme.layout.DefaultCalMongLayout
import com.jingom.calmong.core.designsystem.theme.layout.LocalCalMongLayout
import com.jingom.calmong.core.designsystem.theme.layout.LocalWindowWidthClass
import com.jingom.calmong.core.designsystem.theme.layout.WindowWidthClass
import com.jingom.calmong.core.designsystem.theme.shape.CalMongShapes
import com.jingom.calmong.core.designsystem.theme.shape.DefaultCalMongShapes
import com.jingom.calmong.core.designsystem.theme.shape.LocalCalMongShapes
import com.jingom.calmong.core.designsystem.theme.shape.calMongMaterialShapes
import com.jingom.calmong.core.designsystem.theme.spacing.CalMongSpacings
import com.jingom.calmong.core.designsystem.theme.spacing.DefaultCalMongSpacings
import com.jingom.calmong.core.designsystem.theme.spacing.LocalCalMongSpacings
import com.jingom.calmong.core.designsystem.theme.stroke.CalMongStrokeWidths
import com.jingom.calmong.core.designsystem.theme.stroke.DefaultCalMongStrokeWidths
import com.jingom.calmong.core.designsystem.theme.stroke.LocalCalMongStrokeWidths

@Composable
fun CalMongTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    windowWidthClass: WindowWidthClass = WindowWidthClass.Compact,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        remember(darkTheme) {
            if (darkTheme) darkCalMongColorScheme() else lightCalMongColorScheme()
        }
    val materialColorScheme =
        remember(colorScheme, darkTheme) {
            colorScheme.toMaterialColorScheme(darkTheme)
        }
    val materialShapes = remember { calMongMaterialShapes() }
    val elevations =
        remember(colorScheme, darkTheme) {
            if (darkTheme) {
                darkCalMongElevations(colorScheme)
            } else {
                lightCalMongElevations(colorScheme)
            }
        }
    CompositionLocalProvider(
        LocalCalMongColorScheme provides colorScheme,
        LocalCalMongShapes provides DefaultCalMongShapes,
        LocalCalMongElevations provides elevations,
        LocalCalMongSpacings provides DefaultCalMongSpacings,
        LocalCalMongStrokeWidths provides DefaultCalMongStrokeWidths,
        LocalCalMongLayout provides DefaultCalMongLayout,
        LocalWindowWidthClass provides windowWidthClass,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            shapes = materialShapes,
            content = content,
        )
    }
}

/**
 * CalMong design token 진입점.
 *
 * `CalMongTheme.colors.primary.background.default`,
 * `CalMongTheme.shapes.surface.card`,
 * `CalMongTheme.elevations.raised1`,
 * `CalMongTheme.spacings.inset.default`,
 * `CalMongTheme.strokeWidths.emphasis`,
 * `CalMongTheme.layout.contentMaxWidth.form`,
 * `CalMongTheme.windowWidthClass`처럼 접근한다.
 * Material3 컴포넌트는 [CalMongTheme]이 매핑해준 `MaterialTheme.colorScheme`을 그대로 쓰고,
 * CalMong 고유 토큰이 필요하면 이 accessor를 쓴다.
 */
object CalMongTheme {
    val colors: CalMongColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalCalMongColorScheme.current

    val shapes: CalMongShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalCalMongShapes.current

    val elevations: CalMongElevations
        @Composable
        @ReadOnlyComposable
        get() = LocalCalMongElevations.current

    val spacings: CalMongSpacings
        @Composable
        @ReadOnlyComposable
        get() = LocalCalMongSpacings.current

    val strokeWidths: CalMongStrokeWidths
        @Composable
        @ReadOnlyComposable
        get() = LocalCalMongStrokeWidths.current

    val layout: CalMongLayout
        @Composable
        @ReadOnlyComposable
        get() = LocalCalMongLayout.current

    /** 현재 창 너비 클래스. 화면 골격 분기에 사용. */
    val windowWidthClass: WindowWidthClass
        @Composable
        @ReadOnlyComposable
        get() = LocalWindowWidthClass.current
}

/**
 * semantic 토큰을 Material3 [androidx.compose.material3.ColorScheme]로 매핑.
 * Material 컴포넌트(Button/Card/Surface 등)가 브랜드 색을 자동으로 따르도록 한다.
 *
 * Material 기본 scheme에서 출발해 우리가 의미를 부여한 role만 override한다(`copy`).
 * 콘텐츠(on*)는 채움 위 콘텐츠 규칙을 따른다: 브랜드 채움 위=brand.foreground.default,
 * 컨테이너(옅은 틴트) 위=neutral.foreground.default, 에러 채움 위=static(흰색).
 */
private fun CalMongColorScheme.toMaterialColorScheme(darkTheme: Boolean) =
    (if (darkTheme) darkColorScheme() else lightColorScheme()).copy(
        primary = primary.background.default,
        onPrimary = primary.foreground.default,
        primaryContainer = primary.background.subtle,
        onPrimaryContainer = neutral.foreground.default,
        inversePrimary = primary.background.bold,
        secondary = secondary.background.default,
        onSecondary = secondary.foreground.default,
        secondaryContainer = secondary.background.subtle,
        onSecondaryContainer = neutral.foreground.default,
        // 브랜드가 2색이라 tertiary는 secondary 별칭(같은 색)
        tertiary = secondary.background.default,
        onTertiary = secondary.foreground.default,
        tertiaryContainer = secondary.background.subtle,
        onTertiaryContainer = neutral.foreground.default,
        background = neutral.background.base,
        onBackground = neutral.foreground.default,
        surface = neutral.background.default,
        onSurface = neutral.foreground.default,
        surfaceVariant = neutral.background.raised1,
        onSurfaceVariant = neutral.foreground.subtle,
        // 명시적 elevation을 쓰므로 Material tonal tint는 끈다(표면색과 동일)
        surfaceTint = neutral.background.default,
        surfaceContainerLowest = neutral.background.base,
        surfaceContainerLow = neutral.background.default,
        surfaceContainer = neutral.background.raised1,
        surfaceContainerHigh = neutral.background.raised2,
        surfaceContainerHighest = neutral.background.raised2,
        surfaceBright = neutral.background.default,
        surfaceDim = neutral.background.dimmed,
        inverseSurface = neutral.background.inverted,
        inverseOnSurface = neutral.foreground.inverted,
        error = functional.common.negative.default,
        onError = neutral.foreground.static,
        errorContainer = functional.common.negative.subtle,
        onErrorContainer = neutral.foreground.default,
        outline = neutral.stroke.default.default,
        outlineVariant = neutral.stroke.subtle.default,
        scrim = functional.general.overlay,
    )
